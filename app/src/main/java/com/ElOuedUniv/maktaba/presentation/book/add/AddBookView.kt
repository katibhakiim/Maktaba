package com.ElOuedUniv.maktaba.presentation.book.add

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

/**
 * TP5 – Bonus Task 4.1 / 4.3
 *
 * • Image Picker: a [rememberLauncherForActivityResult] with
 *   [ActivityResultContracts.GetContent] lets the user pick a photo from
 *   the device gallery.
 * • Cover preview: the selected image is shown immediately using Coil's
 *   [AsyncImage] (works for both local content:// URIs and remote https:// URLs).
 * • On confirm: the ViewModel uploads the image to Supabase Storage and saves
 *   the public URL with the book row.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookView(
    onBackClick: () -> Unit,
    viewModel: AddBookViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Bonus Task 4.1 – Gallery image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onAction(AddBookUiAction.OnImageSelected(it)) }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onBackClick()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "ADD BOOK",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.onAction(AddBookUiAction.OnAddClick) },
                        enabled = uiState.isFormValid && !uiState.isUploading
                    ) {
                        val color = if (uiState.isFormValid && !uiState.isUploading)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        Text("Confirm", fontWeight = FontWeight.Bold, color = color)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // ── Bonus Task 4.1: Cover image picker / preview ──────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                onClick = { imagePickerLauncher.launch("image/*") }
            ) {
                if (uiState.selectedImageUri != null) {
                    // Bonus Task 4.3: display the picked image via Coil AsyncImage
                    AsyncImage(
                        model = uiState.selectedImageUri,
                        contentDescription = "Selected cover image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "TAP TO ADD COVER IMAGE",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Choose from gallery",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // ── Input fields ──────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { viewModel.onAction(AddBookUiAction.OnTitleChange(it)) },
                    label = { Text("Book Title") },
                    placeholder = { Text("e.g. Clean Code") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = uiState.titleError != null,
                    supportingText = { uiState.titleError?.let { Text(it) } }
                )

                OutlinedTextField(
                    value = uiState.isbn,
                    onValueChange = { viewModel.onAction(AddBookUiAction.OnIsbnChange(it)) },
                    label = { Text("ISBN") },
                    placeholder = { Text("e.g. 9780132350884") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = uiState.isbnError != null,
                    supportingText = { uiState.isbnError?.let { Text(it) } }
                )

                OutlinedTextField(
                    value = uiState.nbPages,
                    onValueChange = { viewModel.onAction(AddBookUiAction.OnPagesChange(it)) },
                    label = { Text("Pages") },
                    placeholder = { Text("Not set") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = uiState.nbPagesError != null,
                    supportingText = { uiState.nbPagesError?.let { Text(it) } }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Upload progress or Confirm button ─────────────────────────
            if (uiState.isUploading) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Uploading to Supabase…",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Button(
                    onClick = { viewModel.onAction(AddBookUiAction.OnAddClick) },
                    enabled = uiState.isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Confirm", style = MaterialTheme.typography.titleMedium)
                }
            }

            // ── Error message ─────────────────────────────────────────────
            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
