package techzone

/** Datos de prueba. Son los mismos que usa la base de conocimiento de Prolog. */
object Datos {

  val productos: List[Producto] = List(
    Producto(1,  "Laptop Ryzen 7",      "laptops",     3200.0,   12),
    Producto(2,  "Mouse Inalambrico",   "perifericos",   85.0,   40),
    Producto(3,  "Teclado Mecanico",    "perifericos",  250.0,   25),
    Producto(4,  "Memoria RAM 16GB",    "componentes",  320.0,   30),
    Producto(5,  "SSD 1TB",             "componentes",  380.0,   20),
    Producto(6,  "Audifonos Bluetooth", "audio",        180.0,    4),
    Producto(7,  "Webcam HD",           "perifericos",  150.0,    7),
    Producto(8,  "Monitor 27 pulgadas", "monitores",    900.0,    8),
    Producto(9,  "iPhone 15",           "telefonos",   4200.0,    6),
    Producto(10, "Laptop Core i5",      "laptops",     2400.0,    9)
  )

  val ventas: List[Venta] = List(
    Venta(1, 1,  2, "2026-09-01"),
    Venta(2, 2,  5, "2026-09-01"),
    Venta(3, 3,  3, "2026-09-02"),
    Venta(4, 9,  1, "2026-09-03"),
    Venta(5, 4,  4, "2026-09-03"),
    Venta(6, 8,  2, "2026-09-04"),
    Venta(7, 2,  3, "2026-09-05"),
    Venta(8, 10, 1, "2026-09-05")
  )
}