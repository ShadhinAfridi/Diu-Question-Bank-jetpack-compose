package com.fourdevs.diuquestionbank.data

import kotlinx.serialization.Serializable

@Serializable
data class DepartmentData(val name:String, val initial:String, val courseName:String, val faculty:String)

val departments = listOf(
    DepartmentData("Architecture", "B. Arch.", "Bachelor of", "FE"),
    DepartmentData("Business Administration", "BBA", "Bachelor of", "FBE"),
    DepartmentData("Business Studies in E-Business Management", "BS", "Bachelor of", "FBE"),
    DepartmentData("Civil Engineering", "Civil", "B.Sc. in", "FE"),
    DepartmentData("Commerce", "B. Com. (Hons)", "Bachelor of", "FBE"),
    DepartmentData("Computer Science and Engineering", "CSE", "B.Sc. in", "FSIT"),
    DepartmentData("Computing and Information System", "CIS", "B.Sc. in", "FSIT"),
    DepartmentData("Electrical and Electronic Engineering", "EEE", "B.Sc. in", "FE"),
    DepartmentData("English", "BAE", "B.A. (Hons) in", "FHSS"),
    DepartmentData("Entrepreneurship", "BE", "Bachelor of", "FBE"),
    DepartmentData("Environmental Science and Disaster Management", "ESDM", "B.Sc. in", "FSIT"),
    DepartmentData("Information and Communication Engineering", "ICE", "B.Sc. in", "FE"),
    DepartmentData("Information Technology and Management", "ITM", "B.Sc. in", "FSIT"),
    DepartmentData("Journalism Media and Communication", "JMC", "BSS in", "FHSS"),
    DepartmentData("Law", "LLB (Hons)", "Bachelor of", "FHSS"),
    DepartmentData("Multimedia and Creative Technology", "MCT", "B.Sc. in", "FSIT"),
    DepartmentData("Nutrition and Food Engineering", "NFE", "Bachelor of", "FAHS"),
    DepartmentData("Pharmacy", "B. Pharm", "Bachelor of", "FAHS"),
    DepartmentData("Physical Education and Sports Science", "PESS", "B.Sc. in", "FSIT"),
    DepartmentData("Real Estate", "BRE", "Bachelor of", "FBE"),
    DepartmentData("Software Engineering", "SWE", "B.Sc. in", "FSIT"),
    DepartmentData("Textile Engineering", "TE", "B.Sc. in", "FE"),
    DepartmentData("Tourism and Hospitality Management", "BTHM", "Bachelor of", "FBE")
)


fun getCourseList(department:String):List<Course> {
    var courseList = mutableListOf<Course>()
    when (department) {
        "Computer Science and Engineering" ->
            courseList =  cseCourseList
        "Software Engineering" ->
            courseList = sweCourseList
        "Multimedia and Creative Technology" ->
            courseList = mctCourseList
        "Environmental Science and Disaster Management" ->
            courseList = esdmCourseList
        "Computing and Information System" ->
            courseList = cisCourseList
        "Information Technology and Management" ->
            courseList = itmCourseList
        "Physical Education and Sports Science" ->
            courseList = pessCourseList
        "Business Administration" ->
           courseList = bbaCourseList
        "Business Studies in E-Business Management" ->
            courseList = bsCourseList
        "Real Estate" ->
            courseList = reCourseList
        "Tourism and Hospitality Management" ->
            courseList = thmCourseList
        "Entrepreneurship" ->
            courseList = eCourseList
        "English" ->
            courseList = englishCourseList
        "Laws" ->
            courseList = lawCourseList
        "Journalism Media and Communication" ->
            courseList = jmcCourseList
        "Information and Communication Engineering" ->
            courseList = iceCourseList
        "Textile Engineering" ->
            courseList = teCourseList
        "Electrical and Electronic Engineering" ->
            courseList = eeeCourseList
        "Architecture" ->
            courseList = archCourseList
        "Civil Engineering" ->
            courseList = civilCourseList
        "Pharmacy" ->
            courseList = pharmacyCourseList
        "Nutrition and Food Engineering" ->
            courseList = nfeCourseList
        "Public Health" ->
            courseList = phCourseList
    }

    return courseList
}