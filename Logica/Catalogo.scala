package techzone

import NivelStock._

/** Consultas de solo lectura sobre el catalogo. */
object Catalogo {

  /** Base de casi todas las consultas: solo productos activos. */
  def activos(ps: List[Producto]): List[Producto] =
    ps.filter(_.activo)

  def porCategoria(ps: List[Producto], categoria: String): List[Producto] =
    activos(ps).filter(_.categoria.equalsIgnoreCase(categoria))

  def porRangoPrecio(ps: List[Producto], min: Double, max: Double): List[Producto] =
    activos(ps).filter(p => p.precio >= min && p.precio <= max)

  /** Busqueda parcial por nombre, sin distinguir mayusculas. */
  def buscarPorNombre(ps: List[Producto], texto: String): List[Producto] =
    activos(ps).filter(_.nombre.toLowerCase.contains(texto.toLowerCase))

  /** Devuelve Some(producto) o None: nunca null. */
  def buscarPorId(ps: List[Producto], id: Int): Option[Producto] =
    ps.find(_.id == id)

  /** Productos con stock menor al minimo, ordenados de menor a mayor. */
  def stockBajo(ps: List[Producto], minimo: Int): List[Producto] =
    activos(ps).filter(_.stock < minimo).sortBy(_.stock)

  def agotados(ps: List[Producto]): List[Producto] =
    activos(ps).filter(_.stock == 0)

  /** Clasifica el stock con pattern matching. */
  def nivelStock(p: Producto): NivelStock = p.stock match {
    case 0           => Agotado
    case s if s < 5  => Critico
    case s if s < 20 => Bajo
    case _           => Disponible
  }

  def valorInventario(p: Producto): Double =
    p.precio * p.stock

  def valorTotalInventario(ps: List[Producto]): Double =
    activos(ps).map(valorInventario).sum
}