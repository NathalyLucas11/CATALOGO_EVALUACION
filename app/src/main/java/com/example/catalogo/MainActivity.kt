package com.example.catalogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// -------------------------------------------------------------
// MODELO DE DATOS Y ESTADO DE NAVEGACIÓN
// -------------------------------------------------------------
data class ItemData(
    val id: Int,
    val titulo: String,
    val descripcion: String,
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
// CONTROLLER DE NAVEGACIÓN (GESTIÓN DE ESTADO)
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
@Composable
fun PantallaInicio(onNavegarGrid: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Principal",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Bienvenido al Catálogo General",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavegarGrid,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(text = "⮕ VER CATÁLOGO")
        }
    }
}

// -------------------------------------------------------------
// 2. PANTALLA GRID / GALERÍA
// -------------------------------------------------------------
@Composable
fun PantallaGrid(
    onItemClick: (ItemData) -> Unit,
    onVolver: () -> Unit
) {
    // Lista con imágenes específicas para cada elemento
    val listaItems = remember {
        listOf(
            ItemData(1, "SOBREVIVIENDO", "Pablo Neruda", R.drawable.libro1),
            ItemData(2, "CAMARO", "Elegante y comodo", R.drawable.automovil1),
            ItemData(3, "FERRARI", "Rapido y con estilo", R.drawable.automovil2),
            ItemData(4, "TAXI", "Seguro para cualquier viaje", R.drawable.automovil3),
            ItemData(5, "CENICIENTA ONLINE", "Disney web", R.drawable.libro2),
            ItemData(6, "EN LAS ALTURAS", "Daniela Perez", R.drawable.libro3),
            ItemData(7, "APRENDE A LEER", "Escuela Manta", R.drawable.libro4),
            ItemData(8, "APLICACIONES MODERNAS", "Android", R.drawable.libro5)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "⬅ Explora Colecciones",
                style = MaterialTheme.typography.titleLarge
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(listaItems) { item ->
                CardItemGrid(item = item, onClick = { onItemClick(item) })
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onVolver,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Atrás")
        }
    }
}

// -------------------------------------------------------------
// COMPONENTE TARJETA DE LA CUADRÍCULA
// -------------------------------------------------------------
@Composable
fun CardItemGrid(item: ItemData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = item.imagenRes),
                contentDescription = item.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.titulo,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = item?.descripcion ?: "Detalles del elemento seleccionado...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// -------------------------------------------------------------
// 3. PANTALLA DE DETALLE
// -------------------------------------------------------------
@Composable
fun PantallaDetalle(
    item: ItemData?,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "⬅ Detalle del Libro",
            style = MaterialTheme.typography.titleLarge
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(
                        id = item?.imagenRes ?: R.drawable.libro1
                    ),
                    contentDescription = "Imagen Detalle",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = item?.titulo ?: "Información de la Card",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item?.descripcion ?: "Detalles del elemento seleccionado...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = onVolver,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Atrás")
        }
    }
}

// -------------------------------------------------------------
// VISTAS PREVIAS (PREVIEWS)
// -------------------------------------------------------------

@Preview(showBackground = true, showSystemUi = true, name = "Vista Previa Inicio")
@Composable
fun PreviewPantallaInicio() {
    MaterialTheme {
        PantallaInicio(onNavegarGrid = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Vista Previa Grid")
@Composable
fun PreviewPantallaGrid() {
    MaterialTheme {
        PantallaGrid(onItemClick = {}, onVolver = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Vista Previa Detalle")
@Composable
fun PreviewPantallaDetalle() {
    MaterialTheme {
        PantallaDetalle(
            item = ItemData(
                id = 1,
                titulo = "SOBREVIVIENDO",
                descripcion = "Tras la muerte de su esposo, el magnate Matías Wagner, con quien se casó a los quince años estando embarazada, Val decide presentarse a Sobreviviendo, el primer reality show emitido en España.",
                imagenRes = R.drawable.libro1
            ),
            onVolver = {}
        )
    }
}