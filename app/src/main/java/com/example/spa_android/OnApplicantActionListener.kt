package com.example.spa_android

import com.example.spa_android.retrofit.ApplicantModel

interface OnApplicantActionListener {
    fun onAccept(applicant: ApplicantModel)
    fun onReject(applicant: ApplicantModel)
}