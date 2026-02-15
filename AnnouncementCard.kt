@Composable
fun AnnouncementCard(
    announcement: Announcement,
    onDelete: (String) -> Unit
) {
    val categoryInfo = remember(announcement.category) {
        CATEGORY_ICONS[announcement.category] ?: CATEGORY_ICONS["General"]!!
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(16.dp))
    ) {
        if (announcement.isImportant) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.Red)
            )
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = categoryInfo.iconRes),
                    contentDescription = null,
                    tint = Color(categoryInfo.color),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = announcement.category,
                    color = Color(categoryInfo.color),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row {
                if (announcement.isImportant) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "Important",
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                }
                IconButton(onClick = { onDelete(announcement.id) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray
                    )
                }
            }
        }

        Text(
            text = announcement.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        Text(
            text = announcement.content,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp, bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = "Time",
                modifier = Modifier.size(12.dp),
                tint = Color.Gray
            )
            Text(
                text = formatDate(announcement.createdAt),
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
