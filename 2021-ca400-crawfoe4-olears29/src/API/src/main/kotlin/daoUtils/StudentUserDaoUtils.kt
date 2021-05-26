package daoUtils

import StudentUserDaoInstance
import dataClasses.StudentUser

object StudentUserDaoUtils {
    fun studentsInModule(moduleCode: String, studentList: List<StudentUser>) : List<StudentUser> {
        //Not particularly pretty, result of exposed array issue and inexperience
        //Scaling issues?
        val subStudentUserList : MutableList<StudentUser> = mutableListOf()
        for(student in studentList){
            if (student.moduleCodes.contains(moduleCode))
            {
                subStudentUserList.add(student)
            }
        }
        return subStudentUserList
    }

    fun addModuleToStudentUser(studentID: String, moduleCode: String){
        val studentUser = StudentUserDaoInstance.getStudentUser(studentID)
        val moduleList: MutableList<String> = studentUser?.moduleCodes as MutableList<String>
        moduleList.add(moduleCode)
        return StudentUserDaoInstance.updateStudentUser(studentUser)
    }
}