package com.mohdsaifansari.mindtek.Account

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mohdsaifansari.mindtek.Components.NavigationItem

@Composable
fun ProfileItemScreen(navController: NavController, context: Context) {
    val itemName = navController.currentBackStackEntry?.arguments?.getString("item_name")
    Scaffold(topBar = {
        ProfileItemHeader(itemName, navController)
    }) { innerPadding ->
        ProfileMainScreen(itemName, paddingValues = innerPadding)
    }

}

@Composable
fun ProfileMainScreen(
    itemName: String?,
    paddingValues: PaddingValues
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary, // #dee4f4
                        MaterialTheme.colorScheme.background
                    ), start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (itemName == "Privacy Policy") {
                ShowPrivacyPolicyText()
            } else if (itemName == "Contact Us") {
                ShowContactUsText()
            } else {
                ShowAboutMindtekText()
            }
        }
    }
}

@Composable
private fun ShowPrivacyPolicyText() {
    Text(
        text = buildPrivacyPolicyAnnotatedString(), modifier = Modifier
            .padding(16.dp),
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontFamily = FontFamily.Serif,
        softWrap = true,
        textAlign = TextAlign.Justify,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun ShowContactUsText() {
    Text(
        text = buildContactUsAnnotatedString(), modifier = Modifier
            .padding(16.dp),
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontFamily = FontFamily.Serif,
        softWrap = true,
        textAlign = TextAlign.Justify,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun ShowAboutMindtekText() {
    Text(
        text = buildAboutMindTekAnnotatedString(), modifier = Modifier
            .padding(16.dp),
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontFamily = FontFamily.Serif,
        softWrap = true,
        textAlign = TextAlign.Justify,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileItemHeader(itemName: String?, navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            if (itemName != null) {
                Text(
                    text = itemName,
                    modifier = Modifier.padding(5.dp),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            scrolledContainerColor = Color.White
        ), navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        navController.navigate(NavigationItem.HomeScreen.route) {
                            popUpTo(NavigationItem.ProfileItemNav.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    )
}


fun buildPrivacyPolicyAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 21.sp)) {
            append("Terms and Conditions\n")
        }
        append("Effective Date: 01/07/2024\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
            append("Introduction\n")
        }
        append(
            "MindTek (\"we,\" \"us,\" \"our\") is committed to protecting your privacy. " +
                    "This Privacy Policy explains how we collect, use, disclose, and safeguard your " +
                    "information when you use our MindTek AI Tool mobile application (the \"App\"). " +
                    "Please read this policy carefully to understand our views and practices " +
                    "regarding your personal data and how we will treat it.\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("Information We Collect\n")
        }

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Personal Data:\n")
        }
        append("- Contact Information: Email address, phone number.\n")
        append("- Profile Information: Username, profile picture.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Usage Data:\n")
        }
        append("- App Activity: Features used, time spent on the app, and preferences.\n")
        append("- Log Data: IP address, device type, operating system, and app version.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Device Data:\n")
        }
        append("- Device Information: Device identifiers, mobile network information.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("AI Interaction Data:\n")
        }
        append(
            "- Chat Logs: Interaction history with the AI chatbot for improving service quality.\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("How We Use Your Information\n")
        }
        append(
            "We use the information we collect in the following ways:\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("To Provide and Manage the Service:\n")
        }
        append("- Facilitate the creation of accounts and the login process.\n")
        append("- Provide personalized features and content.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("To Improve Our Services:\n")
        }
        append("- Analyze usage patterns to improve app performance and user experience.\n")
        append("- Conduct research and analysis to maintain and enhance our services.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("To Communicate with You:\n")
        }
        append("- Send you updates, security alerts, and support messages.\n")
        append("- Respond to your inquiries and requests.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("To Ensure Security:\n")
        }
        append(
            "- Monitor and analyze trends, usage, and activities to detect and prevent fraudulent activities.\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("How We Share Your Information\n")
        }
        append("We may share your information in the following situations:\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("With Service Providers:\n")
        }
        append(
            "- Third-party vendors and service providers that assist us in providing our services, " +
                    "including cloud storage, data analysis, and customer support.\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("For Legal Reasons:\n")
        }
        append(
            "- When required by law or to protect the rights, property, or safety of MindTek, " +
                    "our users, or others.\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("With Your Consent:\n")
        }
        append("- When you provide explicit consent to share your information with third parties.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("Data Security\n")
        }
        append(
            "We use administrative, technical, and physical security measures to help protect your " +
                    "personal information. However, no method of transmission over the internet or " +
                    "electronic storage is completely secure, and we cannot guarantee its absolute security.\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("Your Rights\n")
        }
        append(
            "Depending on your location, you may have the following rights regarding your personal data:\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Access and Update:\n")
        }
        append("You can access and update your personal information through your account settings.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Deletion:\n")
        }
        append("You can request the deletion of your account and personal data.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Restriction:\n")
        }
        append("You can request that we restrict the processing of your personal data.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Objection:\n")
        }
        append(
            "You can object to our processing of your personal data based on legitimate interests.\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Portability:\n")
        }
        append(
            "You can request a copy of your personal data in a machine-readable format.\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("Changes to This Privacy Policy\n")
        }
        append(
            "We may update this Privacy Policy from time to time. We will notify you of any changes " +
                    "by posting the new Privacy Policy on this page and updating the \"Effective Date\" above. " +
                    "You are advised to review this Privacy Policy periodically for any changes.\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("Contact Us\n")
        }
        append(
            "If you have any questions about this Privacy Policy, please contact us at:\n\n"
        )
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Email:\n")
        }
        append("[support@mindtek.com]\n\n")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Phone:\n")
        }
        append("[7800680076]\n\n")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Address:\n")
        }
        append("[Lucknow, Uttar Pradesh, India]")
    }
}


fun buildAboutMindTekAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
            append("About MindTek\n\n")
        }
        append(
            "MindTek is a cutting-edge AI toolkit designed to enhance productivity and streamline your digital interactions. " +
                    "Our app harnesses the power of artificial intelligence to provide a versatile suite of tools aimed at " +
                    "simplifying complex tasks and improving efficiency for both individuals and businesses.\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)) {
            append("Key Features:\n\n")
        }

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("1. AI-Powered Chatbot: ")
        }
        append("Our intelligent chatbot offers personalized assistance, answering questions, and providing solutions tailored to your needs.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("2. Voice Commands: ")
        }
        append("Hands-free interaction with our app using advanced voice recognition technology.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("3. Data Analysis Tools: ")
        }
        append("Leverage AI to analyze data sets, generate insights, and make informed decisions.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("4. Task Automation: ")
        }
        append("Automate repetitive tasks to save time and reduce manual effort.\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("5. Customizable Widgets: ")
        }
        append("Personalize your app experience with widgets that suit your workflow.\n\n")

        append(
            "At MindTek, we are committed to continuously innovating and improving our services. " +
                    "Our mission is to make advanced AI technology accessible to everyone, helping users achieve more with less effort. " +
                    "Whether you're a professional looking to boost your productivity or a tech enthusiast eager to explore the latest AI advancements, " +
                    "MindTek is your go-to app for intelligent solutions."
        )
    }
}


fun buildContactUsAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
            append("Contact Us\n\n")
        }
        append(
            "We value your feedback and are here to assist you with any questions or concerns you may have about the MindTek AI Tool app. " +
                    "Please feel free to reach out to us through any of the following methods:\n\n"
        )

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("Email:\n")
        }
        append("For general inquiries, support, or feedback, you can contact us at:\n")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color.Blue)) {
            append("[support@mindtek.com]\n\n") // Replace with your actual email
        }

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("Phone:\n")
        }
        append("You can also reach our customer support team by phone:\n")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color.Blue)) {
            append("[7800680076]\n\n") // Replace with your actual phone number
        }

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("Mailing Address:\n")
        }
        append("If you prefer to send us mail, our address is:\n")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color.Blue)) {
            append("[Mindtek]\n")
            append("[Block A]\n")
            append("[Lucknow, Uttar Pradesh, 226010]\n")
            append("[India]\n\n")
        }


        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
            append("Feedback and Suggestions:\n")
        }
        append(
            "We are always looking to improve our app and services. If you have any suggestions or feedback, " +
                    "please do not hesitate to let us know. Your input is invaluable in helping us provide the best " +
                    "possible experience for all our users.\n\n"
        )

        append("Thank you for using MindTek AI Tool. We look forward to hearing from you!\n\n")
        append("---")
    }
}