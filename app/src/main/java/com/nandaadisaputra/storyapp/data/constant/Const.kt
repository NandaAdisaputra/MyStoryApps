package com.nandaadisaputra.storyapp.data.constant

class Const {
    object ERROR {
        const val USERNAME= "Username must be filled"
        const val PASSWORD= "Password must be filled"
        const val DESCRIPTION= "Description must be filled"
        const val EMAIL= "Email must be filled"
        const val EMPATRATUS = "Tolong masukkan email dan password dengan benar"
        const val EMPATRATUSSATU = "Email atau password salah"
        const val ERRORSERVER ="Terdapat masalah pada Server, silahkan tunggu"
        const val ERRORSERVERTWO ="Server Or Connection to Server Error"
    }
    object CONS {
        const val WAITING= "Loading...."

    }
    object SESSION {
        const val BIOMETRIC = "biometric"
    }
    object BIOMETRIC {
        const val BIOMETRICAUTH = "Biometric Authentication"
        const val BIOMETRICCREDENTIALS = "Enter biometric credentials to proceed"
        const val BIOMETRICDESCRIPTION = "Input your Fingerprint or FaceID to ensure it's you"
        const val CANCEL = "Cancel"
    }

    object CONSTANTS {
        const val THEME_KEY = "theme_key"
        const val DARK_MODE_KEY = "dark_mode_key"
    }
    object TITLE {
        const val SETTING= "Setting"
        const val DETAIL= "Detail"
        const val HOME = "Home"
        const val ADD = "Add Story"
    }
    object PICTURE {
        const val CHOOSE = "Choose a Picture"
        const val LOCATIONNOTFOUND = "Location is not found. Try Again"
        const val LOCATIONPERMISSION = "Location permission granted"
        const val UPLOADFAILED = "Upload story failed"
        const val PERMISSIONCAMERA= "You need the camera permission to use this feature"
    }

}