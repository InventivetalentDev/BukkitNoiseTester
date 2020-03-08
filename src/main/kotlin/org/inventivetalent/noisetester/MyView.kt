package org.inventivetalent.noisetester

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.bukkit.util.noise.PerlinNoiseGenerator
import org.bukkit.util.noise.PerlinOctaveGenerator
import org.bukkit.util.noise.SimplexNoiseGenerator
import org.bukkit.util.noise.SimplexOctaveGenerator
import tornadofx.*
import java.util.*
import kotlin.math.abs


class MyView : View("Bukkit Noise Tester") {

    val numberLabelWidth  = 80.0

    var renderSize = SimpleIntegerProperty(16)
    var size = SimpleIntegerProperty(512)

    var generatorType = SimpleStringProperty("Octave")
    var isOctaveGenerator = generatorType.booleanBinding { it.equals("Octave") }
    var isNoiseGenerator = generatorType.booleanBinding { it.equals("Noise") }

    var generator = SimpleStringProperty("Simplex")
    var isSimplexGenerator = generator.booleanBinding { it.equals("Simplex") }
    var isPerlinGenerator = generator.booleanBinding { it.equals("Perlin") }

    var octaves = SimpleIntegerProperty(4)
    var scale = SimpleDoubleProperty(1.0)
    var frequency = SimpleDoubleProperty(0.5)
    var amplitude = SimpleDoubleProperty(0.5)

    var code = SimpleStringProperty()

    override val root = borderpane {
        top {
            vbox {
                alignment = Pos.CENTER

                canvas(512.0, 512.0) {

                    widthProperty().bind(size)
                    heightProperty().bind(size)

                    size.onChange { draw(graphicsContext2D) }
                    renderSize.onChange { draw(graphicsContext2D) }
                    generatorType.onChange { draw(graphicsContext2D) }
                    generator.onChange { draw(graphicsContext2D) }
                    octaves.onChange { draw(graphicsContext2D) }
                    scale.onChange { draw(graphicsContext2D) }
                    frequency.onChange { draw(graphicsContext2D) }
                    amplitude.onChange { draw(graphicsContext2D) }


                    draw(graphicsContext2D)
                }
            }
        }
        bottom {
            vbox {
                form {
                    fieldset {
                        field("Render Size") {
                            textfield(renderSize)
                        }
                        combobox(generatorType, observableListOf("Octave", "Noise"))
                        combobox(generator, observableListOf("Simplex", "Perlin"))

                        field("Octaves") {
                            slider(0, 100, 8) {
                                valueProperty().bindBidirectional(octaves)
                            }
                            textfield(octaves){
                                prefWidth = numberLabelWidth
                                minWidth =numberLabelWidth
                                maxWidth =numberLabelWidth
                            }
                        }
                        field("Scale") {
                            slider(0, 100, 1) {
                                valueProperty().bindBidirectional(scale)
                            }
                            textfield(scale){
                                prefWidth = numberLabelWidth
                                minWidth =numberLabelWidth
                                maxWidth =numberLabelWidth
                            }
                        }
                        field("Frequency") {
                            slider(0, 100, 1) {
                                valueProperty().bindBidirectional(frequency)
                            }
                            textfield(frequency){
                                prefWidth = numberLabelWidth
                                minWidth =numberLabelWidth
                                maxWidth =numberLabelWidth
                            }
                        }
                        field("Amplitude") {
                            slider(0, 100, 1) {
                                valueProperty().bindBidirectional(amplitude)
                            }
                            textfield(amplitude){
                                prefWidth = numberLabelWidth
                                minWidth =numberLabelWidth
                                maxWidth =numberLabelWidth
                            }
                        }
                        separator()
                        field{
                            textarea (code)
                        }
                    }
                }
            }
        }
    }

    fun draw(context: GraphicsContext) {
        val size = size.value

        context.save()

        val s = size / renderSize.value
        context.scale(s.toDouble(), s.toDouble())

        context.fill = Color.GRAY
        context.fillRect(0.0, 0.0, size.toDouble(), size.toDouble())

        var c = generator.value+generatorType.value+"Generator(random";
        if (isOctaveGenerator.value) {
            c+=", ${octaves.value}"
        }
        c+=")\n"
        c+="generator.noise(X, Y, "

        val rand = Random()
        if (generatorType.value == "Octave") {
            val gen = when (generator.value) {
                "Simplex" -> SimplexOctaveGenerator(rand, octaves.value)
                "Perlin" -> PerlinOctaveGenerator(rand, octaves.value)
                else -> SimplexOctaveGenerator(rand, octaves.value)
            }
            drawNoise(size, context) { x, y -> gen.noise(x.toDouble(), y.toDouble(), amplitude.value, frequency.value, true) }

            c+="${amplitude.value}, ${frequency.value}"
        } else if (generatorType.value == "Noise") {
            val gen = when (generator.value) {
                "Simplex" -> SimplexNoiseGenerator(rand)
                "Perlin" -> PerlinNoiseGenerator(rand)
                else -> SimplexNoiseGenerator(rand)
            }
            drawNoise(size, context) { x, y -> gen.noise(x.toDouble(), y.toDouble(), octaves.value, amplitude.value, frequency.value, true) }

            c+="${octaves.value}, ${amplitude.value}, ${frequency.value}"
        }
        c+=")"
        code.set(c)

        context.restore()
    }


    fun drawNoise(size: Int, context: GraphicsContext, noiseProvider: (x: Int, y: Int) -> Double) {
        for (x in 0..size) {
            for (y in 0..size) {
                val n = abs(noiseProvider(x, y))
                context.fill = Color.gray(n)
                context.fillRect(x.toDouble(), y.toDouble(), 1.0, 1.0)
            }
        }
    }

}

class Styles : Stylesheet() {
    companion object {
        val render by cssclass()
    }

    init {
        render {
            minWidth = 500.px

        }
    }
}

class BottomView : View() {
    override val root = label("Bottom View")
}
