package cn.a10miaomiao.bilidown.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cn.a10miaomiao.bilidown.common.UrlUtil
import cn.a10miaomiao.bilidown.db.dao.OutRecord
import coil.compose.AsyncImage

@Composable
fun RecordItem(
    title: String,
    cover: String,
    status: Int,
    onClick: () -> Unit,
    onDeleteClick: (isDeleteFile: Boolean) -> Unit,
) {
    var expandedMoreMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.padding(5.dp),
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Column() {
                Row(
                    modifier = Modifier
                        .clickable(onClick = onClick)
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        model = UrlUtil.autoHttps(cover) + "@672w_378h_1c_",
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 120.dp, height = 80.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .padding(horizontal = 10.dp),
                    ) {
                        Text(
                            text = title,
                            maxLines = 2,
                            modifier = Modifier.weight(1f),
                            overflow = TextOverflow.Ellipsis,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val statusText = when (status) {
                                OutRecord.STATUS_WAIT -> "队列中"
                                OutRecord.STATUS_SUCCESS -> "已导出"
                                OutRecord.STATUS_FAIL -> "导出出现异常"
                                -1 -> "导出文件已被删除"
                                else -> "未导出"
                            }
                            Text(
                                modifier = Modifier.weight(1f),
                                text = statusText,
//                                maxLines = 1,
                                color = if (status >= 0) {
                                    MaterialTheme.colorScheme.outline
                                } else {
                                    Color.Red
                                },
                                overflow = TextOverflow.Ellipsis,
                            )
                            Box() {
                                IconButton(
                                    onClick = { expandedMoreMenu = true }
                                ) {
                                    Icon(Icons.Filled.MoreVert, null)
                                }
                                val menus = remember<List<String>>(status) {
                                    if (status == OutRecord.STATUS_SUCCESS) {
                                        listOf("删除记录", "删除记录及文件")
                                    } else {
                                        listOf("移除任务")
                                    }
                                }
                                DropdownMenu(
                                    expanded = expandedMoreMenu,
                                    onDismissRequest = { expandedMoreMenu = false },
                                ) {
                                    menus.forEachIndexed { index, text ->
                                        DropdownMenuItem(
                                            onClick = {
                                                expandedMoreMenu = false
                                                onDeleteClick(index == 1)
                                            },
                                            text = {
                                                Text(text = text)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}