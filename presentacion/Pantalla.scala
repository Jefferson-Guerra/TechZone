package techzone

import java.util.Locale

/** Todo lo relacionado con mostrar informacion en consola. No contiene logica de negocio. */
object Pantalla {

  private val Ancho = 60

  def titulo(t: String): Unit = {
    val linea = "=" * Ancho
    println()
    println(linea)
    println(s" $t")
    println(linea)
  }

  def dinero(d: Double): String =
    "S/ %,.2f".formatLocal(Locale.US, d)

  def tablaProductos(ps: List[Producto]): Unit =
    if (ps.isEmpty) println("  (sin resultados)")
    else {
      println("  %-4s %-22s %-13s %10s %6s %-10s"
        .formatLocal(Locale.US, "ID", "Nombre", "Categoria", "Precio", "Stock", "Nivel"))
      println("  " + "-" * 70)
      ps.foreach { p =>
        println("  %-4d %-22s %-13s %10.2f %6d %-10s".formatLocal(
          Locale.US, p.id, p.nombre, p.categoria, p.precio, p.stock,
          Catalogo.nivelStock(p).etiqueta))
      }
    }
}