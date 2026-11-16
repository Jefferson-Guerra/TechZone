package techzone

/** Operaciones de almacen. Todas devuelven una lista NUEVA: la original no cambia. */
object Inventario {

  def agregar(ps: List[Producto], nuevo: Producto): List[Producto] =
    ps :+ nuevo

  /** Baja logica: el producto queda inactivo, no se elimina de la lista. */
  def darDeBaja(ps: List[Producto], id: Int): List[Producto] =
    ps.map(p => if (p.id == id) p.copy(activo = false) else p)

  def actualizarStock(ps: List[Producto], id: Int, nuevoStock: Int): List[Producto] =
    ps.map(p => if (p.id == id) p.copy(stock = nuevoStock) else p)

  /** Descuenta stock validando existencia, estado y disponibilidad. */
  def descontarStock(ps: List[Producto], id: Int, cantidad: Int): Either[ErrorVenta, List[Producto]] =
    Catalogo.buscarPorId(ps, id) match {
      case None                          => Left(ErrorVenta.ProductoNoExiste(id))
      case Some(p) if !p.activo          => Left(ErrorVenta.ProductoDadoDeBaja(p.nombre))
      case Some(p) if p.stock < cantidad =>
        Left(ErrorVenta.StockInsuficiente(p.nombre, p.stock, cantidad))
      case Some(p)                       => Right(actualizarStock(ps, id, p.stock - cantidad))
    }
}