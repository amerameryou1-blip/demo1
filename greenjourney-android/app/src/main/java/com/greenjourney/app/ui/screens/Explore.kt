package com.greenjourney.app.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Award
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.greenjourney.app.data.airports
import com.greenjourney.app.data.Airport
import com.greenjourney.app.data.AppState
import com.greenjourney.app.data.AppNotification
import com.greenjourney.app.ui.theme.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

private fun rangeFmt(ts: Long): String {
    val hrs = ((System.currentTimeMillis() - ts) / 3_600_000L).toInt()
    return when {
        hrs < 1 -> "Just now"
        hrs < 24 -> "${hrs}h ago"
        else -> "${hrs / 24}d ago"
    }
}

private fun airportMarkerBitmap(a: Airport, selected: Boolean, d: Float, ctx: Context): BitmapDrawable {
    val w = (150f * d).toInt()
    val h = (66f * d).toInt()
    val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bmp)
    val cx = w / 2f
    val cy = 25f * d
    val mint = android.graphics.Color.parseColor("#34D399")
    val cyan = android.graphics.Color.parseColor("#22D3EE")
    val main = if (selected) cyan else mint

    val glow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader = RadialGradient(cx, cy, 24f * d, main, android.graphics.Color.TRANSPARENT, Shader.TileMode.CLAMP)
    }
    canvas.drawCircle(cx, cy, 24f * d, glow)

    if (selected) {
        val halo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f * d
            color = 0xCC22D3EE.toInt()
        }
        canvas.drawCircle(cx, cy, 16f * d, halo)
    }

    canvas.drawCircle(cx, cy, 7.5f * d, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xB3FFFFFF.toInt() })
    canvas.drawCircle(cx, cy, 6f * d, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = main })

    val label = if (selected) "${a.code} — ${a.city}" else a.code
    val text = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = main
        textSize = (if (selected) 10.5f else 10f) * d
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }
    if (selected) {
        val pw = text.measureText(label) + 18f * d
        val rect = RectF(cx - pw / 2f, 36f * d, cx + pw / 2f, 56f * d)
        canvas.drawRoundRect(rect, 9f * d, 9f * d, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xF20B1425.toInt() })
        canvas.drawRoundRect(rect, 9f * d, 9f * d, Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f * d
            color = 0x7322D3EE.toInt()
        })
        canvas.drawText(label, cx, 51f * d, text)
    } else {
        canvas.drawText(label, cx, 52f * d, text)
    }
    return BitmapDrawable(ctx.resources, bmp)
}

@Composable
fun MapScreen(state: AppState) {
    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(airports[0].code) }
    val focus = airports.find { it.code == selected } ?: airports[0]
    val filtered = airports.filter {
        it.code.contains(query, true) || it.name.contains(query, true) || it.city.contains(query, true)
    }

    var mapView by remember { mutableStateOf<MapView?>(null) }

    DisposableEffect(Unit) {
        onDispose { mapView?.onDetach() }
    }

    fun refreshMarkers(view: MapView) {
        val dens = view.resources.displayMetrics.density
        view.overlays.clear()
        airports.forEach { a ->
            val m = Marker(view)
            m.position = GeoPoint(a.lat, a.lng)
            m.icon = airportMarkerBitmap(a, a.code == selected, dens, view.context)
            m.setAnchor(0.5f, (25f * dens) / (66f * dens))
            m.setOnMarkerClickListener { _, _ -> selected = a.code; true }
            view.overlays.add(m)
        }
        view.controller.animateTo(GeoPoint(focus.lat, focus.lng))
        view.invalidate()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Column {
                Text("GREEN AIRPORTS", color = Color(0xFF67E8F9), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.4.sp)
                Spacer(Modifier.height(4.dp))
                Text("Sustainable Hub Network", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Discover eco-certified airports powered by renewables, SAF, and zero-waste operations.",
                    color = TextSecondary, fontSize = 12.5.sp,
                )
            }
        }

        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF040A18))
                    .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(24.dp))
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        Configuration.getInstance().load(
                            ctx,
                            ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE),
                        )
                        Configuration.getInstance().userAgentValue = ctx.packageName
                        MapView(ctx).apply {
                            setTileSource(
                                XYTileSource(
                                    "CartoDarkMatter",
                                    1, 18, 256, ".png",
                                    arrayOf(
                                        "https://a.basemaps.cartocdn.com/dark_all/",
                                        "https://b.basemaps.cartocdn.com/dark_all/",
                                        "https://c.basemaps.cartocdn.com/dark_all/",
                                        "https://d.basemaps.cartocdn.com/dark_all/",
                                    ),
                                    "© OpenStreetMap contributors © CARTO",
                                )
                            )
                            setMultiTouchControls(true)
                            minZoomLevel = 2.0
                            maxZoomLevel = 16.0
                            controller.setZoom(4.0)
                            controller.setCenter(GeoPoint(focus.lat, focus.lng))
                        }.also { mapView = it }
                    },
                    update = { view -> refreshMarkers(view) },
                )

                Box(Modifier.fillMaxWidth().height(60.dp).align(Alignment.TopStart).background(Brush.verticalGradient(listOf(Color(0xFF040A18), Color(0x80040A18), Color.Transparent))))
                Box(Modifier.fillMaxWidth().height(60.dp).align(Alignment.BottomStart).background(Brush.verticalGradient(listOf(Color.Transparent, Color(0x8002040B), Color(0xFF02040B)))))
                Box(Modifier.fillMaxHeight().width(40.dp).align(Alignment.CenterStart).background(Brush.horizontalGradient(listOf(Color(0xD9040A18), Color.Transparent))))
                Box(Modifier.fillMaxHeight().width(40.dp).align(Alignment.CenterEnd).background(Brush.horizontalGradient(listOf(Color.Transparent, Color(0xD9040A18)))))

                Box(
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xB3030612))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("CartoDB Dark Matter | © OpenStreetMap © CARTO", color = TextMuted, fontSize = 8.sp)
                }
            }
        }

        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x08FFFFFF))
                    .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.Search, null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    textStyle = TextStyle(color = TextPrimary, fontSize = 13.sp),
                    cursorBrush = SolidColor(Cyan),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    decorationBox = { inner ->
                        Box {
                            if (query.isEmpty()) Text("Search by IATA, city or airport…", color = TextMuted, fontSize = 13.sp)
                            inner()
                        }
                    },
                )
            }
        }

        item {
            GlassCard(Modifier.fillMaxWidth(), corner = 18.dp) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Place, null, tint = Color(0xFF67E8F9), modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("FEATURED HUB", color = TextMuted, fontSize = 10.sp, letterSpacing = 1.sp)
                            }
                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(focus.name, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Black)
                                Spacer(Modifier.width(6.dp))
                                Text(focus.code, color = Color(0xFF67E8F9), fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }
                            Text(focus.city, color = TextSecondary, fontSize = 12.sp)
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Mint.copy(alpha = 0.12f))
                                .border(1.dp, Mint.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                        ) {
                            Text("ECOSCORE", color = Mint, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text(focus.score.toString(), color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Black)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        MiniStat("SAF Ready", "Yes", Mint, Modifier.weight(1f))
                        MiniStat("EV Transit", "Rail", Color(0xFF67E8F9), Modifier.weight(1f))
                        MiniStat("Zero-Waste", "A+", Gold, Modifier.weight(1f))
                    }
                }
            }
        }

        items(filtered, key = { it.code }) { a ->
            val active = a.code == selected
            GlassCard(
                Modifier
                    .fillMaxWidth()
                    .clickable { selected = a.code },
                corner = 16.dp,
            ) {
                Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (active) Cyan.copy(alpha = 0.15f) else Color(0x0AFFFFFF))
                                .border(1.dp, if (active) Cyan.copy(alpha = 0.4f) else Color(0x14FFFFFF), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Default.FlightTakeoff, null, tint = if (active) Color(0xFF67E8F9) else TextSecondary, modifier = Modifier.size(20.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(a.code, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("  — ${a.city}", color = TextSecondary, fontSize = 13.sp)
                            }
                            Text(a.name, color = TextMuted, fontSize = 11.sp)
                        }
                    }
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Mint.copy(alpha = 0.10f))
                            .border(1.dp, Mint.copy(alpha = 0.25f), RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Spa, null, tint = Mint, modifier = Modifier.size(12.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(a.score.toString(), color = Mint, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniStat(label: String, value: String, tint: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(tint.copy(alpha = 0.10f))
            .border(1.dp, tint.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp),
    ) {
        Text(label.uppercase(), color = tint.copy(alpha = 0.85f), fontSize = 9.5.sp, letterSpacing = 0.8.sp)
        Text(value, color = tint, fontSize = 13.sp, fontWeight = FontWeight.Black)
    }
}

private data class NotifMeta(val icon: ImageVector, val tint: Color, val label: String)

private fun notifMeta(type: String) = when (type) {
    "points" -> NotifMeta(Icons.Default.Bolt, Mint, "POINTS")
    "reward" -> NotifMeta(Icons.Default.CardGiftcard, Gold, "REWARD")
    "tier" -> NotifMeta(Icons.Default.Award, Color(0xFF67E8F9), "TIER")
    else -> NotifMeta(Icons.Default.EmojiEvents, Rose, "QUEST")
}

@Composable
fun NotificationsScreen(state: AppState) {
    val unread = state.notifications.count { !it.read }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            GlassCard(Modifier.fillMaxWidth(), corner = 24.dp) {
                Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(listOf(Color(0x4022D3EE), Color(0x1A3B82F6))))
                            .border(1.dp, Color(0x4D22D3EE), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Default.Notifications, null, tint = Color(0xFF67E8F9), modifier = Modifier.size(28.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("ALERT HUB", color = Color(0xFF67E8F9), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.4.sp)
                        Text("Recent Notifications", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
                        Spacer(Modifier.height(3.dp))
                        Text(
                            if (unread > 0) "$unread unread updates" else "All caught up. Nice work!",
                            color = TextSecondary, fontSize = 12.sp,
                        )
                    }
                }
            }
        }

        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GhostButton(onClick = { state.markAllNotificationsRead() }, modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.DoneAll, null, tint = TextPrimary, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Mark all read", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                GhostButton(onClick = { state.clearNotifications() }, modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.DeleteOutline, null, tint = Rose, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Clear all", color = Rose, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        if (state.notifications.isEmpty()) {
            item {
                GlassCard(Modifier.fillMaxWidth(), corner = 20.dp) {
                    Column(
                        Modifier.padding(36.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Icon(Icons.Default.Notifications, null, tint = TextMuted, modifier = Modifier.size(28.dp))
                        Text(
                            "Nothing here yet. Verify eco actions to earn alerts.",
                            color = TextSecondary, fontSize = 13.sp, textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        } else {
            items(state.notifications, key = { it.id }) { n ->
                NotificationRow(n)
            }
        }
    }
}

@Composable
private fun NotificationRow(n: AppNotification) {
    val meta = notifMeta(n.type)
    GlassCard(Modifier.fillMaxWidth(), corner = 16.dp) {
        Row(Modifier.padding(14.dp)) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(meta.tint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(meta.icon, null, tint = meta.tint, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(n.title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    if (!n.read) {
                        Box(
                            Modifier
                                .size(7.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(Color(0xFF67E8F9))
                        )
                    }
                }
                Spacer(Modifier.height(3.dp))
                Text(n.message, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(50))
                            .background(meta.tint.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(meta.label, color = meta.tint, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(rangeFmt(n.timestamp), color = TextMuted, fontSize = 10.5.sp)
                }
            }
        }
    }
}
