@file:Suppress("unused")

package app.github1552980358.hadean.root

/**
 * [RootHelper]
 * @author  : 1552980328
 * @since   : 0.1
 * @date    : 2020/8/24
 * @time    : 16:23
 **/

interface RootHelper {
    
    companion object {
        
        const val COMMAND_HEAD = "/system/bin/pm disable "
        
    }
    
    fun disableApp(packageName: String) = disableApp(mutableListOf(packageName))
    
    fun disableApp(packageNames: MutableList<String>): List<String>? {
        for ((i, j) in packageNames.withIndex()) {
            packageNames[i] = COMMAND_HEAD + j
        }
        return runCommand(packageNames)
    }
    
    fun disableApp(packageNames: Array<String>) = disableApp(packageNames.toMutableList())
    
    fun runCommand(commands: MutableList<String>): List<String>? {
        var list: List<String>?
        try {
            Runtime.getRuntime().exec("su").apply {
                try {
                    outputStream?.use { outputStream ->
                        outputStream.bufferedWriter().use { bufferedWriter ->
                            commands.forEach { command ->
                                try {
                                    bufferedWriter.write(command)
                                    bufferedWriter.newLine()
                                } catch (e: Exception) {
                                }
                            }
                            try {
                                bufferedWriter.write("exit")
                                bufferedWriter.newLine()
                                bufferedWriter.flush()
                            } catch (e: Exception) {
                            }
                        }
                        outputStream.flush()
                    }
                } catch (e: Exception) {
                }
                try {
                    waitFor()
                } catch (e: Exception) {
                    return null
                }
                try {
                    inputStream.use { inputStream ->
                        inputStream.bufferedReader().use { bufferedReader ->
                            // Get list from output, otherwise set as null
                            list = try {
                                bufferedReader.readLines()
                            } catch (e: Exception) {
                                null
                            }
                        }
                    }
                } catch (e: Exception) {
                    return null
                }
            }
        } catch (e: Exception) {
            return null
        }
        return list
    }
    
}
