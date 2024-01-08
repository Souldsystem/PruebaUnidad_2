package com.example.pruebaunidad_2

import com.example.pruebaunidad_2.data.modelo.Producto
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebaunidad_2.ui.theme.viewmodels.ProductoViewModel

import kotlinx.coroutines.delay
import java.util.UUID


class MainActivity : ComponentActivity() {

    private val ProductoVm: ProductoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("MainActivity::onCreate", "Recuperando Productos en disco")
        try {
            ProductoVm.obtenerProductosGuardadosEnDisco(openFileInput(ProductoViewModel.FILE_NAME))
        } catch (e:Exception) {
            Log.e("MainActivity::onCreate", "Archivo con Productos no existe!!")
        }

        setContent {
            AppProductos()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.v("MainActivity::onPause", "Guardando a disco")
        ProductoVm.guardarProductosEnDisco(openFileOutput(ProductoViewModel.FILE_NAME, MODE_PRIVATE))
    }

    override fun onStop() {
        super.onStop()
        Log.v("MainActivity::onStop", "Guardando a disco")

    }
    }

@Composable
fun AppProductos(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomePageUI(
                onButtonSettingsClicked = {
                    navController.navigate("settings")
                }
            )
        }
        composable("settings") {
            SettingsPageUI(
                onBackButtonClicked = {
                    navController.popBackStack()
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppProductosTopBar(
    title:String = "",
    showSettingsButton:Boolean = true,
    onButtonSettingsClicked:() -> Unit = {},
    showBackButton:Boolean = false,
    onBackButtonClicked:() -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if(showBackButton) {
                IconButton(onClick = {
                    onBackButtonClicked()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás"
                    )
                }
            }
        },
        actions = {
            if( showSettingsButton ) {
                IconButton(onClick = {
                    onButtonSettingsClicked()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Configuración"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview(showSystemUi = true , locale = "es")
@Composable
fun SettingsPageUI(
    onBackButtonClicked:() -> Unit = {}
) {
    var seDebeOrdenarAlfabeticamente by rememberSaveable {
        mutableStateOf(false)
    }
    var seDebeMostrarPrimeroPorComprar by rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            AppProductosTopBar(
                title = "Configuración",
                showSettingsButton = false,
                showBackButton = true,
                onBackButtonClicked = onBackButtonClicked
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 10.dp,
                    vertical = it.calculateTopPadding()
                )
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text("Ordenar alfabéticamente")
                Switch(
                    checked = seDebeOrdenarAlfabeticamente,
                    onCheckedChange = {
                        seDebeOrdenarAlfabeticamente = it
                    })}
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text("Mostrar primero items por comprar")
                Switch(
                    checked = seDebeMostrarPrimeroPorComprar,
                    onCheckedChange = {
                        seDebeMostrarPrimeroPorComprar = it
                    })
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true, locale = "es")
@Composable
fun HomePageUI(
    ProductosVm: ProductoViewModel = viewModel(),
    onButtonSettingsClicked:() -> Unit = {}
) {
    val contexto = LocalContext.current
    val textoLogo = contexto.getString(R.string.logo)
    val uiState by ProductosVm.uiState.collectAsStateWithLifecycle()
    var mostrarMensaje by rememberSaveable {
        mutableStateOf(false)
    }
    var primeraEjecucion by rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(uiState.mensaje) {
        if(!primeraEjecucion) {
            mostrarMensaje = true
            delay(2_000)
            mostrarMensaje = false
        }
        primeraEjecucion = false
    }

    Scaffold(
        topBar = {
            AppProductosTopBar(
                title = "Lista de Compras",
                onButtonSettingsClicked = onButtonSettingsClicked
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedVisibility(
                visible = mostrarMensaje,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = uiState.mensaje,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(10.dp)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = textoLogo,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            ProductoFormUI {
                ProductosVm.agregarProducto(it)
            }
            Spacer(modifier = Modifier.height(20.dp))
            ProductoListaUI(
                Productos = uiState.productos,
                onCheckboxClicked = uiState.seleccionado,
                onDelete = {
                    ProductosVm.eliminarProducto(it)
                }
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormUI(
    onClickAgregarProducto:(Producto:String) -> Unit
) {
    val contexto = LocalContext.current
   // val textTaskPlaceholder = contexto.getString(R.string.Producto_form_ejemplo)
    val textButtonAddTask = contexto.getString(R.string.Producto_form_agregar)

    val (descripcionProducto, setDescripcionProducto) = rememberSaveable {
        mutableStateOf("")
    }
    Box(
        contentAlignment = Alignment.CenterEnd
        ,modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {


        TextField(
            value = descripcionProducto,
            onValueChange = {
                setDescripcionProducto(it)
            },
           // placeholder = {Text(textTaskPlaceholder)},
            modifier = Modifier.fillMaxWidth()
        )


        TooltipBox(
            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
            tooltip = { PlainTooltip {Text(textButtonAddTask)} },
            state = rememberTooltipState()) {
            IconButton(onClick = {
                Log.v("ProductoFormUI::IconButton", "Agregar Producto")
                val nuevoProducto = Producto(
                    id = UUID.randomUUID().toString(),
                    descripcion = descripcionProducto,
                    seleccionado = false // Establecer el checkbox a false por defecto
                )
                onClickAgregarProducto(descripcionProducto)
                setDescripcionProducto("")
            }) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = textButtonAddTask,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }
    }
}

@Composable
fun ProductoListaUI(
    ProductosVm: ProductoViewModel = viewModel(),
    Productos: List<Producto>,
    onDelete: (t: Producto) -> Unit,
    onCheckboxClicked: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(Productos) { producto ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = producto.seleccionado,
                    onCheckedChange = { isChecked ->
                        ProductosVm.actualizarEstadoProducto(producto, isChecked) // Llama a la función de actualización del ViewModel
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = producto.descripcion,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp, 8.dp),
                    color = if (producto.seleccionado) Color.Gray else Color.Black,
                    textDecoration = if (producto.seleccionado) TextDecoration.LineThrough else TextDecoration.None
                )
            /*    Text(
                    text = producto.descripcion,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp, 8.dp)
                )*/

                IconButton(
                    onClick = { onDelete(producto) },
                    modifier = Modifier.size(25.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar producto",
                        tint = Color.Black
                    )
                }
            }
            HorizontalDivider()
        }
    }
}


@Composable
fun ProductoListaItemUI(
    Producto: Producto,
    onDelete: (t:Producto) -> Unit
) {
    val contexto = LocalContext.current
    val textoEliminarProducto = contexto.getString(R.string.Producto_form_eliminar)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = Producto.descripcion,
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(2.0f)
                    .padding(10.dp, 8.dp)
            )
            IconButton(onClick = {
                Log.v("ProductoListaItemUI::IconButton", "onClick DELETE")
                onDelete(Producto)
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = textoEliminarProducto,
                    tint = Color.Black,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
        HorizontalDivider()
    }
}

