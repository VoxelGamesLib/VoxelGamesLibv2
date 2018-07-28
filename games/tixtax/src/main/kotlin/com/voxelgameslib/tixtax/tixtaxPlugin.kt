package com.voxelgameslib.tixtax

import com.voxelgameslib.kvgl.*
import com.voxelgameslib.voxelgameslib.module.ModuleInfo
import com.voxelgameslib.voxelgameslib.plugin.VGLPlugin

@ModuleInfo(name = "tixtax", authors = ["MiniDigger"], version = "1.0.0")
class tixtaxPlugin : VGLPlugin() {

    override fun getGameMode() = GAMEMODE

    companion object {
        val GAMEMODE = newGameMode<tixtaxGame>("tixtax")
    }
}
