package com.rhythm7.timepayment.plugin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.rhythm7.timepayment.eachFileRecurse
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.codehaus.groovy.runtime.ResourceGroovyMethods
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream

internal class TimePaymentPlugin : Transform(), Plugin<Project> {

    override fun getName() = "Rhythm7.TimePayment"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    override fun isIncremental() = false

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_PROJECT

    override fun apply(project: Project) {
        val android = project.extensions.getByType(AppExtension::class.java)
        android.registerTransform(this)
    }

    override fun transform(transformInvocation: TransformInvocation) {
        transformInvocation.inputs.forEach { input ->

            input.directoryInputs.forEach {directoryInput ->

                if (directoryInput.file.isDirectory) {
                    directoryInput.file.eachFileRecurse {
                        val name = it.name
                        if (name.endsWith(".class") && !name.startsWith("R\$") &&
                                !"R.class".equals(name) && !"BuildConfig.class".equals(name)) {
                            println("$name is changing...")

                            val classReader = ClassReader(ResourceGroovyMethods.getBytes(it))
                            val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                            val classVisitor = TimePaymentClassVisitor(classWriter)
                            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)

                            FileOutputStream("${it.parentFile.absoluteFile}${File.separator}$name")
                                    .use {
                                        it.write(classWriter.toByteArray())
                                    }

                        }
                    }
                }

                val dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

            input.jarInputs.forEach{
                var jarName = it.name
                val md5Name = DigestUtils.md5Hex(it.file.absolutePath)
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length - 4)
                }

                val dest = transformInvocation.outputProvider.getContentLocation(jarName + md5Name,
                        it.contentTypes, it.scopes, Format.JAR)

                FileUtils.copyFile(it.file, dest)
            }
        }

    }
}


