package com.fourdevs.diuquestionbank.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fourdevs.diuquestionbank.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController:NavHostController) {
    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(navController = navController, name = "Edit profile")
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                EditProfile()
            }
        }
    )

}

@Composable
fun EditProfile() {
    var name by remember {
        mutableStateOf("Afridi")
    }
    var email by remember {
        mutableStateOf("shadhinafridi@gmail.com")
    }
    var department by remember {
        mutableStateOf("CSE")
    }
    var about by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.TopCenter)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier.wrapContentSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.flower),
                        contentDescription = "UserName",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(shape = CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(color = Color.LightGray, shape = CircleShape)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Add Image",
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
            }

            TextFiledWithUnderLine(
                label = "Name",
                value = name,
                readonly = false,
                icon = Icons.Outlined.Person
            ) { text ->
                name = text
            }
            TextFiledWithUnderLine(
                label = "Email",
                value = email,
                readonly = true,
                icon = Icons.Outlined.Email
            ) { text ->
                email = text
            }
            DropDownDepartment(department = department) { option ->
                department = option
            }
            TextFiledWithUnderLine(
                label = "About",
                value = about,
                readonly = false,
                icon = Icons.Outlined.Edit
            ) { text ->
                about = text
            }
        }
        FilledTonalButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp)
                .align(Alignment.BottomCenter)

            ,
            colors = ButtonDefaults
                .filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            shape = RoundedCornerShape(5.dp),
            content = {
                Text(
                    text = "Update",
                    color = Color.White,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            },
            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFiledWithUnderLine(
    label: String,
    value: String,
    readonly: Boolean,
    icon: ImageVector,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        label = { Text(text = label) },
        readOnly = readonly,
        leadingIcon = { Icon(imageVector = icon, contentDescription = label) },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownDepartment(department:String, onValueChange: (String) -> Unit){
    val departments = stringArrayResource(id = R.array.department_name).toList().distinct()

    var expanded by remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            readOnly = true,
            value = department,
            onValueChange = {},
            label = { Text(text = "Department") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent),
            leadingIcon = { Icon(imageVector = Icons.Outlined.Place, contentDescription = "Department") },


            )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            departments.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onValueChange(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}