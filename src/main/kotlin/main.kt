import pt.isel.canvas.*
import java.io.File

data class Aresta(val v1: Char, val v2: Char, val cost: Int)

val arestas = mutableListOf<Aresta>()
val vertices = mutableListOf<Char>()
val costs = mutableListOf<Int>()
val finalCosts = mutableListOf<Int>()
var curIdx = 0

const val infinite = 100000

fun readGrafo(){
    val file = File("src/main/resources/Grafo.txt")

    for(line in file.readLines()){
        val info = line.replace(" ", "").split(',', '-')
        val v1 = info[0].single()
        val v2 = info[1].single()
        val cost = info[2].toInt()

        arestas.add(Aresta(v1, v2, cost))

        if(v1 !in vertices) vertices.add(v1)
        if(v2 !in vertices) vertices.add(v2)
    }

    //Organizar alfabeticamente
    vertices.sort()
}

fun main() {
    readGrafo()

    //Adicionar custo inicial ∞ (representado por infinite)
    repeat(vertices.size){ costs.add(infinite); finalCosts.add(infinite) }

    //Input
    print("Vértice inicial: ")
    val initial = readLine()!!.trim().single()
    curIdx = vertices.indexOf(initial)
    costs[curIdx] = 0

    print("Vértice final: ")
    val final = readLine()!!.trim().single()
    val finalIdx = vertices.indexOf(final)

    println(vertices)

    val arena = Canvas(vertices.size * 40, vertices.size * 40, WHITE)

    onStart {
        //Iterar |vertices| vezes
        repeat(vertices.size){ verticeIdx ->
            arena.drawCosts(verticeIdx)
            iterate(vertices[curIdx])
        }

        //Desenhar linha que separa labels dos vértices dos custos
        arena.drawLine(0, 21, arena.width, 21, BLACK, 1)

        println(finalCosts)
        println("O menor custo de $initial a $final é ${finalCosts[finalIdx]}")
    }
    onFinish {

    }

}

/**
 * Desenhar custos de cada iteração em linha
 */
fun Canvas.drawCosts(verticeIdx: Int){
    drawText(40 * verticeIdx, 20, vertices[verticeIdx].toString(), BLACK, 20)

    costs.forEachIndexed { idx, cost ->
        drawText(40 * idx, 20 * verticeIdx + 40,
            when{
                cost == 100000 -> "∞"
                cost < 0 -> "-"
                else -> cost.toString()
            },
            BLACK, 20)
    }
}

/**
 * Em cada iteração, verificam-se quais os vértices adjacentes ao atual e atualiza-se a lista de custos.
 */
fun iterate(current: Char){
    finalCosts[curIdx] = costs[curIdx]

    val adjacencies = adjacencies(current)

    adjacencies.forEach { adjacency ->
        //Novo custo é igual ao custo da aresta (adjacência) mais o custo do vértice atual.
        val cost = adjacency.cost + costs[vertices.indexOf(current)]
        val verticeIdx = vertices.indexOf(adjacency.v)

        //Se o novo custo é menor, atualizar
        if(cost < costs[verticeIdx]) {
            costs[verticeIdx] = cost
        }
    }

    //Nunca mais voltamos a verificar custos do vértice atual (custo passa a -, guardado como -1)
    costs[vertices.indexOf(current)] = -1

    //Atualizar qual o próximo vértice (vértice de menor custo da linha)
    curIdx = idxOfmin(costs)
}

data class Adjancency(val v: Char, val cost: Int)

/**
 * Retorna lista de adjacências (lista de vértices adjacentes a um dado vértice e custo da aresta incidente entre eles)
 */
fun adjacencies(v: Char) : List<Adjancency>{
    val adjacencies = mutableListOf<Adjancency>()

    for(aresta in arestas){
        if(aresta.v1 == v){ adjacencies.add(Adjancency(aresta.v2, aresta.cost)) }
        if(aresta.v2 == v){ adjacencies.add(Adjancency(aresta.v1, aresta.cost)) }
    }

    return adjacencies.distinct()
}

/**
 * Calcula índice do vértice de menor custo da lista de custos
 */
fun idxOfmin(costs: List<Int>) : Int{
    var min = infinite
    var minIdx = 0

    costs.forEachIndexed { idx, cost ->
        if(cost in 1 until min){
            min = cost
            minIdx = idx
        }
    }

    return minIdx
}