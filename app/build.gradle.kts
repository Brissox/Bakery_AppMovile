plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.prueba"
    compileSdk = 36

    defaultConfig {
        namespace = "com.example.prueba"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose: BOM único y reciente
    implementation(platform("androidx.compose:compose-bom:2024.09.02"))

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")   // <- aquí vive animateItemPlacement
    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("com.google.code.gson:gson:2.10.1")

    // Navegación
    implementation("androidx.navigation:navigation-compose:2.8.2")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation("com.google.firebase:firebase-auth-ktx")

    // Tests / debug
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.02"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Capturar imagen camara
    implementation("io.coil-kt:coil-compose:2.6.0")

    //Room (local storage)
    val room_version = "2.8.1"

    implementation("androidx.room:room-runtime:$room_version")

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp("androidx.room:room-compiler:$room_version")

    // If this project only uses Java source, use the Java annotationProcessor
    // No additional plugins are necessary
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:$room_version")

    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$room_version")

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$room_version")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")

    // Para implementar consumo de API
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines + lifecycle
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    // Moshi
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coil (si quieres previsualizar imagenes en Compose)
    implementation("io.coil-kt:coil-compose:2.6.0")


    //Configuración para test unitarios
    // Kotest framework de testing para Kotlin
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0") // Permite ejecutar los tests de Kotest en JUnit 5, porque Android Studio por defecto usa JUnit.
    testImplementation("io.kotest:kotest-assertions-core:5.8.0") // Asserts -> verificaciones

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0") // JUnit (JUnit 5), el estándar de pruebas en Java/Kotlin.

    // MockK
    testImplementation("io.mockk:mockk:1.13.10") // librería para crear: mocks (objetos falsos), stubs (respuestas falsas), spies, verificación de llamadas

    // Compose UI Test
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.2") // Permite renderizar Composables en tests, buscar nodos en la UI, hacer clicks, validar textos, correr en un emulador real
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.2") // Necesario para que el entorno de pruebas tenga actividad

    // Coroutines testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1") // Testear funciones suspend {} sin depender de delays reales.
}

// Obligatorio para usar JUnit 5, habilita JUnit Platform, permite correr Kotest, permite mezclar JUnit 4 y JUnit 5 si deseas
tasks.withType<Test> {
    useJUnitPlatform()
}