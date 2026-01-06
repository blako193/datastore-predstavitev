package com.example.datastoretest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var toggle1: SwitchMaterial
    private lateinit var toggle2: SwitchMaterial
    private lateinit var toggle3: SwitchMaterial
    private lateinit var valuesText: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Inicializacija PreferencesManager
        preferencesManager = PreferencesManager(this)
        
        // Povezava z UI elementi
        toggle1 = findViewById(R.id.toggle1)
        toggle2 = findViewById(R.id.toggle2)
        toggle3 = findViewById(R.id.toggle3)
        valuesText = findViewById(R.id.valuesText)
        
        // Nalaganje shranjenih vrednosti
        loadToggleStates()
        
        // Poslušalci za spremembe toggle switchov
        setupToggleListeners()
        
        // Prikaz trenutnih vrednosti
        displayCurrentValues()
    }
    
    private fun loadToggleStates() {
        // Nalaganje vrednosti toggle 1
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferencesManager.toggle1Flow.collect { value ->
                    toggle1.isChecked = value
                }
            }
        }
        
        // Nalaganje vrednosti toggle 2
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferencesManager.toggle2Flow.collect { value ->
                    toggle2.isChecked = value
                }
            }
        }
        
        // Nalaganje vrednosti toggle 3
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferencesManager.toggle3Flow.collect { value ->
                    toggle3.isChecked = value
                }
            }
        }
    }
    
    private fun setupToggleListeners() {
        toggle1.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                preferencesManager.saveToggle1(isChecked)
            }
        }
        
        toggle2.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                preferencesManager.saveToggle2(isChecked)
            }
        }
        
        toggle3.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                preferencesManager.saveToggle3(isChecked)
            }
        }
    }
    
    private fun displayCurrentValues() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    preferencesManager.toggle1Flow,
                    preferencesManager.toggle2Flow,
                    preferencesManager.toggle3Flow
                ) { toggle1, toggle2, toggle3 ->
                    Triple(toggle1, toggle2, toggle3)
                }.collect { (t1, t2, t3) ->
                    valuesText.text = """
                        Toggle 1: ${if (t1) "ON" else "OFF"}
                        Toggle 2: ${if (t2) "ON" else "OFF"}
                        Toggle 3: ${if (t3) "ON" else "OFF"}
                    """.trimIndent()
                }
            }
        }
    }
}