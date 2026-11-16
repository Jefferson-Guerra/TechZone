package techzone

import java.util.Locale

object Main {

  def main(args: Array[String]): Unit = {
    val productos = Datos.productos
    val ventas    = Datos.ventas

    println("*** TECHZONE - Sistema de Gestion y Analisis de Productos ***")

    // ---- Consultas de catalogo ----
    Pantalla.titulo("1. CATALOGO COMPLETO")
    Pantalla.tablaProductos(productos)

    Pantalla.titulo("2. FILTRO POR CATEGORIA: perifericos")
    Pantalla.tablaProductos(Catalogo.porCategoria(productos, "perifericos"))

    Pantalla.titulo("3. FILTRO POR RANGO DE PRECIO: 100 a 500")
    Pantalla.tablaProductos(Catalogo.porRangoPrecio(productos, 100, 500))

    Pantalla.titulo("4. BUSQUEDA POR NOMBRE: 'laptop'")
    Pantalla.tablaProductos(Catalogo.buscarPorNombre(productos, "laptop"))

    Pantalla.titulo("5. BUSQUEDA POR ID (Option)")
    List(2, 99).foreach { id =>
      Catalogo.buscarPorId(productos, id) match {
        case Some(p) =>
          println(s"  ID $id -> ${p.nombre} | ${p.categoria} | ${Pantalla.dinero(p.precio)} | stock ${p.stock}")
        case None =>
          println(s"  ID $id -> no encontrado")
      }
    }

    // ---- Control de stock ----
    Pantalla.titulo("6. CONTROL DE STOCK (stock menor a 10)")
    Pantalla.tablaProductos(Catalogo.stockBajo(productos, 10))

    println()
    val agotados = Catalogo.agotados(productos)
    if (agotados.isEmpty) println("  Productos agotados: ninguno")
    else agotados.foreach(p => println(s"  AGOTADO: ${p.nombre}"))

    // ---- Valor del inventario ----
    Pantalla.titulo("7. VALOR DEL INVENTARIO")
    Catalogo.buscarPorId(productos, 1).foreach { p =>
      println(s"  Valor de '${p.nombre}': ${Pantalla.dinero(Catalogo.valorInventario(p))}")
    }
    println(s"  Valor total del inventario: ${Pantalla.dinero(Catalogo.valorTotalInventario(productos))}")

    // ---- Ventas ----
    Pantalla.titulo("8. REGISTRO DE VENTAS")
    println("  %-6s %-22s %5s %-12s %12s"
      .formatLocal(Locale.US, "Venta", "Producto", "Cant", "Fecha", "Importe"))
    println("  " + "-" * 62)
    ventas.foreach { v =>
      val nombre  = Catalogo.buscarPorId(productos, v.idProducto).map(_.nombre).getOrElse("?")
      val importe = Ventas.importe(v, productos).map(Pantalla.dinero).getOrElse("-")
      println("  %-6d %-22s %5d %-12s %12s".formatLocal(
        Locale.US, v.idVenta, nombre, v.cantidad, v.fecha, importe))
    }

    Pantalla.titulo("9. INDICADORES DE VENTAS")
    ventas.headOption.flatMap(Ventas.importe(_, productos)).foreach { imp =>
      println(s"  Importe de la venta 1:   ${Pantalla.dinero(imp)}")
    }
    println(s"  Ingreso total:           ${Pantalla.dinero(Ventas.ingresoTotal(ventas, productos))}")
    println(s"  Ticket promedio:         ${Pantalla.dinero(Ventas.ticketPromedio(ventas, productos))}")
    println(s"  Ventas del 2026-09-03:   ${Ventas.porFecha(ventas, "2026-09-03").size} transacciones")

    Pantalla.titulo("10. INGRESOS POR CATEGORIA")
    println("  %-14s %9s %15s".formatLocal(Locale.US, "Categoria", "Unidades", "Ingresos"))
    println("  " + "-" * 40)
    Ventas.porCategoria(ventas, productos).foreach { r =>
      println("  %-14s %9d %15s".formatLocal(Locale.US, r.categoria, r.unidades, Pantalla.dinero(r.ingresos)))
    }

    Pantalla.titulo("11. PRODUCTOS MAS VENDIDOS (top 3)")
    Ventas.masVendidos(ventas, productos, 3).zipWithIndex.foreach { case ((nombre, unidades), i) =>
      println(s"  ${i + 1}. $nombre - $unidades unidades")
    }

    // ---- Gestion con inmutabilidad ----
    Pantalla.titulo("12. GESTION (inmutabilidad)")
    val tablet    = Producto(11, "Tablet Pro 11", "tablets", 1800.0, 15)
    val conTablet = Inventario.agregar(productos, tablet)
    val sinWebcam = Inventario.darDeBaja(conTablet, 7)

    println(s"  Lista original: ${productos.size} productos | Lista nueva: ${conTablet.size} productos")
    println(s"  Webcam HD dada de baja. Activos en lista nueva: ${sinWebcam.count(_.activo)}")
    println(s"  Webcam HD en la lista original sigue activa: ${productos.find(_.id == 7).exists(_.activo)}")

    // ---- Registro de ventas con validacion (foldLeft en lugar de var) ----
    Pantalla.titulo("13. REGISTRAR VENTAS CON VALIDACION")
    val pedidos = List((2, 5), (6, 10), (99, 1), (7, 1))

    val (stockFinal, ventasFinales) =
      pedidos.foldLeft((sinWebcam, ventas)) { case ((ps, vs), (id, cant)) =>
        Ventas.registrar(ps, vs, id, cant, "2026-09-06") match {
          case Right((nuevosPs, nuevasVs)) =>
            println(s"  OK    -> venta de $cant unidad(es) del producto $id")
            (nuevosPs, nuevasVs)
          case Left(error) =>
            println(s"  ERROR -> ${error.mensaje}")
            (ps, vs)
        }
      }

    println()
    println(s"  Ventas registradas en la sesion: ${ventasFinales.size - ventas.size}")
    println(s"  Stock final de Mouse Inalambrico: ${stockFinal.find(_.id == 2).map(_.stock).getOrElse(0)}")

    println()
    println("*** FIN DEL PROGRAMA ***")
  }
}