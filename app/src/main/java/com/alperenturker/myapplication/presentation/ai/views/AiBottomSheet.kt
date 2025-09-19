package com.alperenturker.myapplication.presentation.ai.views


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alperenturker.myapplication.presentation.ai.AiViewModel
import com.alperenturker.myapplication.presentation.ui.theme.Dark


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AiBottomSheet(
    initialQuery: String,
    vm: AiViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onUseSuggestion: (String) -> Unit
) {
    val state = vm.state.collectAsState().value
    val scroll = rememberScrollState()
    var hiddenGems by remember { mutableStateOf(false) }

    // ---- Seçenekler ----
    val genreOptions = listOf(
        "Aksiyon", "Bilim Kurgu", "Dram", "Komedi", "Korku",
        "Gerilim", "Suç", "Fantastik", "Animasyon", "Romantik", "Macera", "Belgesel"
    )
    val yearOptions = listOf("2000 öncesi", "2000–2010", "2010–2020", "2020 ve sonrası")
    val ratingOptions = listOf("6+", "7+", "8+", "9+")
    val paceOptions = listOf("Sakin", "Orta", "Tempolu")
    val countOptions = listOf("5", "8", "10")

    var primaryGenre by remember { mutableStateOf<String?>(null) }
    var extraGenres by remember { mutableStateOf(setOf<String>()) }
    var yearRange by remember { mutableStateOf<String?>(null) }
    var imdbThreshold by remember { mutableStateOf<String?>(null) }
    var pace by remember { mutableStateOf<String?>(null) }
    var count by remember { mutableStateOf("5") }
    var extraNote by remember {
        // opsiyonel serbest metin
        mutableStateOf(if (initialQuery.isNotBlank()) "Benzer filmler: $initialQuery" else "")
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Dark.Surface,
        modifier = Modifier.fillMaxHeight(0.95f),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Dark.OnDim) }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(scroll) // <-- scroll
                .imePadding()                  // <-- klavye açılınca yukarı it
                .navigationBarsPadding()       // <-- nav bar için güvenli alan
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("AI Film Önerileri", style = MaterialTheme.typography.titleMedium, color = Dark.OnBg)

            // 1) Tür (tek seçim)
            SectionCard("Tür / Konu") {
                DropdownField(
                    label = "Tür / Konu seçin",
                    options = genreOptions,
                    value = primaryGenre,
                    onSelected = { primaryGenre = it }
                )
            }

            // 2) Ek türler (çoklu)
            SectionCard("Ek Türler") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    genreOptions.forEach { g ->
                        val selected = g in extraGenres
                        FilterChip(
                            selected = selected,
                            onClick = {
                                extraGenres = if (selected) extraGenres - g else extraGenres + g
                            },
                            label = { Text(g) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Dark.Surface2,
                                labelColor = Dark.OnBg,
                                selectedContainerColor = Dark.Surface2,
                                selectedLabelColor = Dark.OnBg
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selected
                            )
                        )
                    }
                }
            }


            // 3) Yıl aralığı
            SectionCard("Yıl Aralığı") {
                DropdownField(
                    label = "Yıl aralığı seçin",
                    options = yearOptions,
                    value = yearRange,
                    onSelected = { yearRange = it }
                )
            }

            // 4) IMDb eşiği
            SectionCard("IMDb Eşiği") {
                DropdownField(
                    label = "IMDb puanı (en az)",
                    options = ratingOptions,
                    value = imdbThreshold,
                    onSelected = { imdbThreshold = it }
                )
            }


            // 5) Tempo
            SectionCard("Tempo") {
                var local by remember { mutableStateOf(pace ?: "Orta") }
                Segmented(
                    items = paceOptions,
                    value = local,
                    onValueChange = { local = it; pace = it },
                    labelOf = { it }
                )
            }

            // 6) Kaç öneri?
            SectionCard("Öneri Sayısı") {
                var local by remember { mutableStateOf(count) }
                Segmented(
                    items = countOptions,
                    value = local,
                    onValueChange = { local = it; count = it },
                    labelOf = { it }
                )
            }

            // 7) Gizli mücevher tercihi
            SectionCard("Tercihler") {
                HiddenGemsRowPretty(
                    checked = hiddenGems,
                    onCheckedChange = { hiddenGems = it }
                )
            }


            // 8) Ek not (opsiyonel)
            SectionCard("Ek Not (opsiyonel)") {
                OutlinedTextField(
                    value = extraNote,
                    onValueChange = { extraNote = it },
                    placeholder = {
                        Text("ör. karanlık atmosfer • neo-noir • yavaş tempo", color = Dark.OnDim)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Dark.Surface2,
                        unfocusedContainerColor = Dark.Surface2,
                        focusedBorderColor = Dark.Outline,
                        unfocusedBorderColor = Dark.Outline,
                        focusedTextColor = Dark.OnBg,
                        unfocusedTextColor = Dark.OnBg,
                        cursorColor = Dark.OnBg
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val prompt = buildPromptFromFilters(
                            count = count.toInt(),
                            primaryGenre = primaryGenre,
                            extraGenres = extraGenres.toList(),
                            yearRange = yearRange,
                            imdbThreshold = imdbThreshold,
                            pace = pace,
                            preferHiddenGems = hiddenGems,
                            extraNote = extraNote.ifBlank { null }
                        )
                        vm.run(prompt)
                    },
                    enabled = !state.loading,
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .weight(2f)
                        .height(52.dp)
                ) {
                    if (state.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Dark.OnBg,
                            trackColor = Dark.Surface2,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(10.dp))
                        Text("Yükleniyor...")
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.PlayArrow,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Öneri İste")
                    }
                }
            }

            when {
                state.loading -> {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        CircularProgressIndicator(color = Dark.OnBg, trackColor = Dark.Surface2)
                    }
                }
                state.error != null -> {
                    Text("Hata: ${state.error}", color = Color(0xFFFF6B6B))
                }
                state.data != null -> {
                    Spacer(Modifier.height(4.dp))
                    Text("Önerilenler", style = MaterialTheme.typography.labelLarge, color = Dark.OnDim)
                    Spacer(Modifier.height(6.dp))

                    state.data.items.forEach { item ->
                        ElevatedCard(
                            colors = CardDefaults.elevatedCardColors(containerColor = Dark.Surface2),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(
                                    text = buildString {
                                        append(item.title)
                                        item.year?.let { append(" ($it)") }
                                    },
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Dark.OnBg
                                )
                                if (!item.reason.isNullOrBlank()) {
                                    Spacer(Modifier.height(4.dp))
                                    Text(item.reason!!, style = MaterialTheme.typography.bodySmall, color = Dark.OnDim)
                                }
                                Spacer(Modifier.height(8.dp))
                                Row {
                                    TextButton(onClick = { onUseSuggestion(item.title) }) {
                                        Text("Bu başlıkla ara")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

/* ---------------- Helpers ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    options: List<String>,
    value: String?,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            readOnly = true,
            value = value ?: "",
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Dark.Surface2,
                unfocusedContainerColor = Dark.Surface2,
                focusedBorderColor = Dark.Outline,
                unfocusedBorderColor = Dark.Outline,
                focusedTextColor = Dark.OnBg,
                unfocusedTextColor = Dark.OnBg,
                cursorColor = Dark.OnBg
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt) },
                    onClick = {
                        onSelected(opt)
                        expanded = false
                    }
                )
            }
        }
    }
}
@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .padding( vertical = 10.dp)
            .background(Dark.Surface2, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleSmall, color = Dark.OnBg)
        Spacer(Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun <T> Segmented(
    items: List<T>,
    value: T,
    onValueChange: (T) -> Unit,
    labelOf: (T) -> String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Dark.Surface2, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { item ->
            val selected = item == value
            Surface(
                color = if (selected) Dark.Surface else Color.Transparent,
                shape = RoundedCornerShape(10.dp),
                tonalElevation = if (selected) 2.dp else 0.dp,
                modifier = Modifier.weight(1f)
            ) {
                TextButton(
                    onClick = { onValueChange(item) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        labelOf(item),
                        color = if (selected) Dark.OnBg else Dark.OnDim,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun HiddenGemsRowPretty(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AssistChip(
            onClick = { onCheckedChange(!checked) },
            label = { Text("Gizli Mücevherler") },
            leadingIcon = { Text("💎") },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = if (checked) Dark.Surface else Dark.Surface2,
                labelColor = if (checked) Dark.OnBg else Dark.OnDim
            )
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "Popüler yerine az bilinen kaliteli yapımları öne çıkar.",
            color = Dark.OnDim,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

/**
 * Seçimlerden modele gönderilecek doğal-dil prompt’u üretir.
 * (Repository’deki mevcut sistem yönergesi JSON döndürmeyi zaten zorluyor.)
 */
private fun buildPromptFromFilters(
    count: Int,
    primaryGenre: String?,
    extraGenres: List<String>,
    yearRange: String?,
    imdbThreshold: String?,
    pace: String?,
    preferHiddenGems: Boolean,
    extraNote: String?
): String {
    val parts = mutableListOf<String>()
    parts += "$count adet film öner"
    primaryGenre?.let { parts += "Tür: $it" }
    if (extraGenres.isNotEmpty()) parts += "Ek türler: ${extraGenres.joinToString()}"
    yearRange?.let { parts += "Yıl: $it" }
    imdbThreshold?.let { parts += "IMDb: en az $it" }
    pace?.let { parts += "Tempo: $it" }
    if (preferHiddenGems) parts += "Popüler olmayan ‘gizli mücevher’ filmlere öncelik ver"
    extraNote?.let { parts += "Not: $it" }

    // modele biraz daha netlik:
    return buildString {
        append(parts.joinToString(" • "))
        append(". Seçimlerde kült/kaliteli yapımlara dikkat et. Yanıtı SADECE JSON olarak döndür.")
    }
}
