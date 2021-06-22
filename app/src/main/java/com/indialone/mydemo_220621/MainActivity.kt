package com.indialone.mydemo_220621

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.indialone.mydemo_220621.enum.BoxPosition
import com.indialone.mydemo_220621.ui.theme.MyDemo_220621Theme
import com.indialone.mydemo_220621.ui.theme.ShimmerColorShades
import kotlin.math.absoluteValue


class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyDemo_220621Theme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        repeat(5) {
                            item {
                                ShimmerAnimation()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShimmerItem(brush: Brush) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .size(250.dp)
                .background(brush = brush)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .padding(vertical = 8.dp)
                .background(brush = brush)
        )

    }
}

@Composable
fun ShimmerAnimation() {
    val transition = rememberInfiniteTransition()

    val transitionAnim by transition.animateFloat(
        initialValue = 0F,
        targetValue = 2000F,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = ShimmerColorShades,
        start = Offset(10F, 10F),
        end = Offset(transitionAnim, transitionAnim)
    )

    ShimmerItem(brush = brush)

}


@Composable
fun MoveTheBox() {

    var boxPosition by remember { mutableStateOf(BoxPosition.TopLeft) }

    val transition = updateTransition(targetState = boxPosition)

    val boxOffset by transition.animateOffset { position ->
        when (position) {
            BoxPosition.TopLeft -> Offset(0F, 0F)
            BoxPosition.TopRight -> Offset(120F, 0F)
            BoxPosition.BottomLeft -> Offset(0F, 120F)
            BoxPosition.BottomRight -> Offset(120F, 120F)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = {
            boxPosition = getNextPosition(boxPosition)
        }) {
            Text(text = "Move the Box", color = Color.White)
        }

        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier
                .height(150.dp)
                .width(150.dp)
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .offset(boxOffset.x.dp, boxOffset.y.dp)
                    .size(30.dp)
                    .background(Color.Yellow)
            )
        }
    }
}

fun getNextPosition(position: BoxPosition) = when (position) {
    BoxPosition.TopLeft -> BoxPosition.BottomRight
    BoxPosition.BottomRight -> BoxPosition.TopRight
    BoxPosition.TopRight -> BoxPosition.BottomLeft
    BoxPosition.BottomLeft -> BoxPosition.TopLeft
}

@Composable
fun RotateBox() {
    var rotate by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (rotate) 360F else 0F,
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { rotate = !rotate }
        ) {
            Text(text = "Rotate the Box")
        }

        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .animateContentSize()
                .rotate(rotationAngle)
                .background(Color.Red)
        )

    }


}

@Composable
fun ResizeBoxWithButton() {
    var size by remember { mutableStateOf(Size(100F, 100F)) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Box(
            modifier = Modifier
                .animateContentSize()
                .size(size.height.dp)
                .background(Color.Yellow)
        )

        Spacer(modifier = Modifier.height(100.dp))

        Button(
            onClick = {
                size = if (size.height == 100F) Size(200F, 200F) else Size(100F, 100F)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Cyan),
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
        ) {
            Text(text = "Click me to resize the box", color = Color.White)
        }

    }

}

@ExperimentalAnimationApi
@Composable
fun AnimateBoxOnButtonClick() {
    var visible by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Visible or Invisible the box with below button click",
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { visible = !visible },
            Modifier
                .wrapContentWidth()
                .wrapContentHeight()
        ) {
            Text(text = "Click Me to Show/Hide Box")
        }

        Spacer(modifier = Modifier.height(20.dp))

        //visible or invisible of Box

        AnimatedVisibility(visible = visible) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .background(Color.Blue),
            )
        }

    }


}

@ExperimentalAnimationApi
@Composable
fun AnimateBox() {
    var expandedSecurity by remember { mutableStateOf(false) }
    var expandedMails by remember { mutableStateOf(false) }
    var expandedUsers by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Card(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(Color.DarkGray)
                .clickable { expandedSecurity = !expandedSecurity }
                .padding(10.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .background(Color.DarkGray),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                AnimatedVisibility(visible = expandedSecurity) {
                    Text(text = "Security", color = Color.White, fontSize = 20.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Card(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(Color.DarkGray)
                .clickable { expandedMails = !expandedMails }
                .padding(10.dp),
            shape = RoundedCornerShape(10.dp)
        ) {

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .background(Color.DarkGray),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                AnimatedVisibility(visible = expandedMails) {
                    Text(text = "Mails", color = Color.White, fontSize = 20.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Card(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(Color.DarkGray)
                .clickable { expandedUsers = !expandedUsers }
                .padding(10.dp),
            shape = RoundedCornerShape(10.dp)
        ) {

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .background(Color.DarkGray),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                AnimatedVisibility(visible = expandedUsers) {
                    Text(text = "Users", color = Color.White, fontSize = 20.sp)
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimationEditableWithManualParameters() {
    /*
        val editable : Boolean  by remember { mutableStateOf(true) }

        AnimatedVisibility(visible = editable) {
            Text(text = "Edit")
        }
    */


    var visible by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { visible = !visible },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Click me to expand",
                fontSize = 40.sp,
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    initialOffsetY = { with(density) { -40.dp.roundToPx() } }
                ) + expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                Text(
                    "Hello",
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .align(Alignment.CenterHorizontally),
                    fontSize = 50.sp
                )
            }
        }
    }

}

@Composable
fun PrintTextFromEditText() {
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Enter your Name") },
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(20.dp))

//        OutlinedButton(
//            onClick = {
//
//            },
//            modifier = Modifier.width(80.dp),
//            border = BorderStroke(2.dp, Color.DarkGray),
//            shape = RoundedCornerShape(10.dp),
//            elevation = ButtonDefaults.elevation(10.dp)
//        )

        if (name.isNotEmpty()) {
            Text(
                text = name,
                modifier = Modifier.wrapContentWidth(),
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )
        }

    }


}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyDemo_220621Theme {
        MoveTheBox()
    }
}