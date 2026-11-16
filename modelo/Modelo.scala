package techzone

/** Producto del catalogo (laptops, perifericos, componentes, etc.). */
final case class Producto(
    id: Int,
    nombre: String,
    categoria: String,
    precio: Double,
    stock: Int,
    activo: Boolean = true
)

/** Transaccion de venta. Equivale al hecho venta/4 de Prolog. */
final case class Venta(
    idVenta: Int,
    idProducto: Int,
    cantidad: Int,
    fecha: String
)

/** Resumen de ventas agrupado por categoria. */
final case class ResumenCategoria(categoria: String, unidades: Int, ingresos: Double)

/** Nivel de stock. Mismos umbrales que estado_stock/2 en Prolog. */
sealed trait NivelStock {
  def etiqueta: String
}

object NivelStock {
  case object Agotado    extends NivelStock { val etiqueta = "AGOTADO" }     // stock = 0
  case object Critico    extends NivelStock { val etiqueta = "CRITICO" }     // 0 < stock < 5
  case object Bajo       extends NivelStock { val etiqueta = "BAJO" }        // 5 <= stock < 20
  case object Disponible extends NivelStock { val etiqueta = "DISPONIBLE" }  // stock >= 20
}

/** Errores posibles al registrar una venta. */
sealed trait ErrorVenta {
  def mensaje: String
}

object ErrorVenta {
  final case class ProductoNoExiste(id: Int) extends ErrorVenta {
    def mensaje: String = s"Producto $id no existe"
  }

  final case class ProductoDadoDeBaja(nombre: String) extends ErrorVenta {
    def mensaje: String = s"Producto '$nombre' esta dado de baja"
  }

  final case class StockInsuficiente(nombre: String, disponible: Int, pedido: Int) extends ErrorVenta {
    def mensaje: String = s"Stock insuficiente de '$nombre' (hay $disponible, se pidio $pedido)"
  }
}