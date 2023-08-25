package com.fourdevs.diuquestionbank.ui.components

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController


@Composable
fun AboutUsScreen(navController: NavController) {
    AboutUsPage(navController = navController)
}

@Composable
fun AboutUsPage(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBarWithBackIcon(navController = navController, name = "About Us")
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            AboutUsWebView()
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AboutUsWebView() {
    val htmlContent = """
        <!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us - FourDevs</title>
</head>
<body align="justify">
    <header>
        <h1>FourDevs: Empowering Your Digital Presence</h1>
        <p>Welcome to FourDevs, a dynamic team of experts dedicated to creating impactful Android applications, captivating web designs, and optimizing online visibility through SEO. As a versatile digital solutions provider, we blend creativity, technology, and strategy to shape exceptional online experiences.</p>
    </header>

    <section>
        <h2>Our Expertise</h2>
        <p>At FourDevs, we are more than just Android app developers. We are a holistic digital partner, encompassing a spectrum of services to cater to your diverse needs. Our seasoned professionals are adept at Android app development, web design, development, and SEO strategies that drive organic growth.</p>
    </section>

    <section>
        <h2>Why Choose Us?</h2>
        <ul>
            <li><strong>Comprehensive Solutions:</strong> With our multi-faceted approach, you gain access to a complete suite of digital services under one roof.</li>
            <li><strong>Innovative Mindset:</strong> We thrive on innovation. Our team's ability to think beyond boundaries ensures your projects are infused with creative ideas that make them stand out in the digital landscape.</li>
            <li><strong>User-Centric Approach:</strong> Understanding your target audience is our mantra. Our solutions are crafted with user behavior and preferences in mind, delivering experiences that resonate deeply.</li>
            <li><strong>Strategic SEO:</strong> We know the importance of visibility. Our SEO experts employ proven strategies to optimize your online presence, placing you at the forefront of search engine results and driving organic traffic.</li>
            <li><strong>Collaborative Partnership:</strong> We believe in collaboration. Our approach involves working closely with you to comprehend your goals, aligning them with our technical prowess to achieve remarkable outcomes.</li>
        </ul>
    </section>

    <section>
        <h2>Our Services</h2>
        <ul>
            <li><strong>Android App Development:</strong> Our apps redefine user engagement. With intuitive interfaces and seamless functionality, we create Android applications that leave a lasting impact.</li>
            <li><strong>Web Design and Development:</strong> Websites are digital storefronts. Our designers and developers create visually appealing and user-friendly websites that leave an indelible impression.</li>
            <li><strong>Custom Solutions:</strong> No two businesses are alike. We specialize in tailored solutions that cater to your unique requirements, ensuring your digital presence mirrors your brand identity.</li>
            <li><strong>SEO Strategies:</strong> Visibility matters. Our SEO strategies enhance your search engine rankings, enabling potential customers to discover your offerings organically.</li>
            <li><strong>Maintenance and Support:</strong> Our commitment extends beyond project completion. We provide ongoing maintenance and support to ensure your digital assets function flawlessly.</li>
        </ul>
    </section>

    <footer>
        <p>Connect with us today to embark on a journey of digital transformation with FourDevs. Experience the synergy of innovation, design, and optimization - all delivered with an unwavering commitment to excellence.</p>
    </footer>
</body>
</html>
    """.trimIndent()

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            }
        },
        modifier = Modifier.fillMaxSize().padding(10.dp)
    )
}



