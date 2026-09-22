package com.example.catalogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color azul temático
val AzulApp = Color(0xFF1565C0)
val VerdeFondoImagen = Color(0xFFA5D6A7)

// -------------------------------------------------------------
// MODELO DE DATOS
// -------------------------------------------------------------
data class ItemData(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val subtituloOrAutor: String,
    val descripcion: String,
    val rating: String,
    val precioOrYear: String,
    val genero: String,
    val imagenRes: Int
)

enum class Pantalla {
    INICIO,
    GRID,
    DETALLE
}

// -------------------------------------------------------------
// ACTIVITY PRINCIPAL
// -------------------------------------------------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppMainScreen()
            }
        }
    }
}

// -------------------------------------------------------------
// CONTROLLER DE NAVEGACIÓN
// -------------------------------------------------------------
@Composable
fun AppMainScreen() {
    var pantallaActual by remember { mutableStateOf(Pantalla.INICIO) }
    var itemSeleccionado by remember { mutableStateOf<ItemData?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (pantallaActual) {
            Pantalla.INICIO -> PantallaInicio(
                onNavegarGrid = { pantallaActual = Pantalla.GRID }
            )
            Pantalla.GRID -> PantallaGrid(
                onItemClick = { item ->
                    itemSeleccionado = item
                    pantallaActual = Pantalla.DETALLE
                },
                onVolver = { pantallaActual = Pantalla.INICIO }
            )
            Pantalla.DETALLE -> PantallaDetalle(
                item = itemSeleccionado,
                onVolver = { pantallaActual = Pantalla.GRID }
            )
        }
    }
}

// -------------------------------------------------------------
// 1. PANTALLA DE INICIO
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(onNavegarGrid: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Catalogia",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AzulApp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Bienvenido al\nCatálogo General",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Explora y descubre detalles sobre múltiples categorías. Tu guía móvil interactiva.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = onNavegarGrid,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulApp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "VER CATÁLOGO (Grid)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 2. PANTALLA GRID / GALERÍA
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaGrid(
    onItemClick: (ItemData) -> Unit,
    onVolver: () -> Unit
) {
    val listaItems = remember {
        listOf(
            ItemData(1, "Sapiens: De Animales a Dioses", "Libros", "Yuval Noah Harari", "Un recorrido por la historia de la humanidad desde la edad de piedra hasta el siglo XXI.", "4.5★", "2011", "Historia / Antropología", R.drawable.libro1),
            ItemData(2, "Tesla Model S", "Automóviles", "Eléctrico", "Sedán 100% eléctrico con aceleración impresionante y gran autonomía.", "4.8★", "Price: $80 - $200", "Eléctrico", R.drawable.automovil1),
            ItemData(3, "Ferrari F8", "Automóviles", "Rápido y con estilo", "Deportivo italiano con motor V8 biturbo de alto rendimiento.", "4.9★", "Price: $300 - $400", "Deportivo", R.drawable.automovil2),
            ItemData(4, "Taxi Urbano", "Automóviles", "Seguro para cualquier viaje", "Servicio de transporte eficiente y seguro para la ciudad.", "4.2★", "Price: $5 - $20", "Transporte", R.drawable.automovil3),
            ItemData(5, "Cenicienta Online", "Libros", "Disney Web", "Adaptación moderna y digital del clásico cuento de hadas.", "4.0★", "2020", "Infantil", R.drawable.libro2),
            ItemData(6, "En las Alturas", "Libros", "Daniela Pérez", "Una novela apasionante sobre superación personal en los Andes.", "4.6★", "2018", "Ficción", R.drawable.libro3),
            ItemData(7, "Aprende a Leer", "Libros", "Escuela Manta", "Guía práctica de lectoescritura interactiva para niños.", "4.7★", "2022", "Educación", R.drawable.libro4),
            ItemData(8, "Aplicaciones Modernas", "Libros", "Android Compose", "Aprende a construir GUIs modernas usando Jetpack Compose y Kotlin.", "4.9★", "2024", "Tecnología", R.drawable.libro5)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explora Colecciones", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
        ) {
            items(listaItems) { item ->
                CardItemGrid(item = item, onClick = { onItemClick(item) })
            }
        }
    }
}

// -------------------------------------------------------------
// TARJETA DE LA CUADRÍCULA
// -------------------------------------------------------------
@Composable
fun CardItemGrid(item: ItemData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Contenedor superior con fondo verde claro
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(VerdeFondoImagen)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                // Badge de categoría arriba a la izquierda
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = item.categoria,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Image(
                    painter = painterResource(id = item.imagenRes),
                    contentDescription = item.titulo,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(60.dp)
                )
            }

            // Detalles del texto
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = item.titulo,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtituloOrAutor,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (item.categoria == "Automóviles") item.precioOrYear else item.rating,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 3. PANTALLA DE DETALLE
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(
    item: ItemData?,
    onVolver: () -> Unit
) {
    val currentItem = item ?: ItemData(
        1, "Sapiens: De Animales a Dioses", "Libros", "Yuval Noah Harari",
        "Un realistic paragraph describing human evolution and societal development across millenia.",
        "4.5★ (1,234 Reseñas)", "2011", "Historia / Antropología", R.drawable.logo
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Libro", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Recadro superior verde claro con la imagen
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(VerdeFondoImagen, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = currentItem.imagenRes),
                    contentDescription = currentItem.titulo,
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título
            Text(
                text = currentItem.titulo,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de metadata con iconos
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoRow(icon = Icons.Default.Person, text = "Autor: ${currentItem.subtituloOrAutor}")
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                InfoRow(icon = Icons.Default.DateRange, text = "Publicado: ${currentItem.precioOrYear}")
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                InfoRow(icon = Icons.Default.ThumbUp, text = "Género: ${currentItem.genero}")
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                InfoRow(icon = Icons.Default.Star, text = "${currentItem.rating} (1,234 Reseñas)")
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sinopsis
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Sinopsis",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currentItem.descripcion,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón Añadir a Favoritos
            TextButton(
                onClick = { /* Acción favoritos */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Añadir a Favoritos", color = AzulApp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// -------------------------------------------------------------
// PREVIEWS
// -------------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun PreviewPantallaInicio() {
    MaterialTheme { PantallaInicio(onNavegarGrid = {}) }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaGrid() {
    MaterialTheme { PantallaGrid(onItemClick = {}, onVolver = {}) }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaDetalle() {
    MaterialTheme { PantallaDetalle(item = null, onVolver = {}) }
}