package techzone

/** Calculos sobre ventas. */
object Ventas {

  /** Importe de una venta (precio * cantidad). None si el producto no existe. */
  def importe(v: Venta, ps: List[Producto]): Option[Double] =
    Catalogo.buscarPorId(ps, v.idProducto).map(_.precio * v.cantidad)

  def ingresoTotal(vs: List[Venta], ps: List[Producto]): Double =
    vs.flatMap(importe(_, ps)).sum

  def ticketPromedio(vs: List[Venta], ps: List[Producto]): Double =
    if (vs.isEmpty) 0.0 else ingresoTotal(vs, ps) / vs.size

  def porFecha(vs: List[Venta], fecha: String): List[Venta] =
    vs.filter(_.fecha == fecha)

  /** Ingresos y unidades por categoria, ordenados de mayor a menor ingreso. */
  def porCategoria(vs: List[Venta], ps: List[Producto]): List[ResumenCategoria] =
    vs.groupBy(v => categoriaDe(v, ps))
      .map { case (cat, lista) =>
        ResumenCategoria(
          categoria = cat,
          unidades  = lista.map(_.cantidad).sum,
          ingresos  = lista.flatMap(importe(_, ps)).sum
        )
      }
      .toList
      .sortBy(-_.ingresos)

  /** Productos mas vendidos: lista de (nombre, unidades). */
  def masVendidos(vs: List[Venta], ps: List[Producto], top: Int): List[(String, Int)] =
    vs.groupBy(_.idProducto).toList
      .map { case (id, lista) => (nombreDe(id, ps), lista.map(_.cantidad).sum) }
      .sortBy { case (_, unidades) => -unidades }
      .take(top)

  /**
   * Registra una venta: valida y descuenta stock, y agrega la venta al historial.
   * Devuelve el nuevo estado de productos y ventas, o el error encontrado.
   */
  def registrar(
      ps: List[Producto],
      vs: List[Venta],
      idProducto: Int,
      cantidad: Int,
      fecha: String
  ): Either[ErrorVenta, (List[Producto], List[Venta])] =
    Inventario.descontarStock(ps, idProducto, cantidad).map { nuevosPs =>
      val nuevaVenta = Venta(proximoId(vs), idProducto, cantidad, fecha)
      (nuevosPs, vs :+ nuevaVenta)
    }

  private def proximoId(vs: List[Venta]): Int =
    vs.map(_.idVenta).maxOption.getOrElse(0) + 1

  private def categoriaDe(v: Venta, ps: List[Producto]): String =
    Catalogo.buscarPorId(ps, v.idProducto).map(_.categoria).getOrElse("S/C")

  private def nombreDe(id: Int, ps: List[Producto]): String =
    Catalogo.buscarPorId(ps, id).map(_.nombre).getOrElse("Desconocido")
}