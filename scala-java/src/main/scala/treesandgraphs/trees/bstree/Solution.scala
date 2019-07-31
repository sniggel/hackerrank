package treesandgraphs.trees.bstree

sealed trait Node

case class BSTNode(data: Int, left: Option[BSTNode] = None, right: Option[BSTNode] = None) extends Node {

  override def toString: String = toTree("", "").mkString("\n")

  // http://www.geekviewpoint.com/java/bst/dsw_algorithm

  def size: Int = {
    val leftSize = left.fold(0) { l =>
      l.size
    }
    val rightSize = right.fold(0) { r =>
      r.size
    }
    leftSize + rightSize + 1
  }

  /************************************************************************
    *   Before      After
    *    Gr          Gr
    *     \           \
    *     Par         Ch
    *    /  \        /  \
    *   Ch   Z      X   Par
    *  /  \            /  \
    * X    Y          Y    Z
    ***********************************************************************/
  private def rotateRight(grandParent: BSTNode, leftChild: Option[BSTNode]): BSTNode = {
    val newParent = this.copy(left = leftChild.flatMap(_.right))
    val newLeftChild = leftChild.map(x => x.copy(right = Some(newParent)))
    grandParent.copy(right = newLeftChild)
  }

  private def rotateLeft(grandParent: BSTNode, rightChild: Option[BSTNode]): BSTNode = {
    val newParent = this.copy(right = rightChild.flatMap(_.left))
    val newRightChild = rightChild.map(x => x.copy(left = Some(newParent)))
    grandParent.copy(left = newRightChild)
  }

  private def toTree(prefix: String, childrenPrefix: String): Seq[String] = {
    val firstLine = prefix + s"$data"

    val firstChildren = left.fold(Seq.empty[String]) { l =>
      l.toTree(childrenPrefix + "└── ", childrenPrefix + "    ")
    }
    val lastChild = right.fold(Seq.empty[String]) { r =>
     r.toTree(childrenPrefix + "├── ", childrenPrefix + "|    ")
    }
    firstLine +: lastChild ++: firstChildren
  }

  def insert(dataInsert: Int): BSTNode = {
    if (dataInsert > data) {
      left.fold(this.copy(left = Some(BSTNode(dataInsert)))) { l =>
        this.copy(left = Some(l.insert(dataInsert)))
      }
    } else {
      right.fold(this.copy(right = Some(BSTNode(dataInsert)))) { r =>
        this.copy(right = Some(r.insert(dataInsert)))
      }
    }
  }

  def console: Unit = {
    simplePrint(data, "root")
  }

  private def simplePrint(parent: Int, t: String): Unit = {
    print(s"($parent)-> ($data $t) ")
    if (left.isDefined) left.get.simplePrint(data, "l")
    if (right.isDefined) right.get.simplePrint(data, "r")
  }

}

class BinaryTree(numbers: List[Int]) {

  val bst: BSTNode = createBst(BSTNode(numbers.head), numbers.tail)

  private def createBst(root: BSTNode, numbers: List[Int]): BSTNode = {
    numbers match {
      case Nil => root
      case head :: Nil => root.insert(head)
      case head :: tail => createBst(root.insert(head), tail)
    }
  }

  /**
    * Time complexity: log(n)
    * return the index of most significant set bit: index of
    * least significant bit is 0
    */
  def msb(n: Int, idx: Int): Int = {
    if (1 < n) msb(n >> 1, idx + 1)
    else idx
  }

  def greatestPowerOf2LessThanN(n: Int): Int = {
    1<<msb(n, 0)
  }

  def printBst: Unit = {
    print(bst)
  }

  def leftRightSizePrint: Unit = {
    println
    if (bst.left.isDefined) println(s"left size: ${bst.left.get.size}")
    if (bst.right.isDefined) println(s"right size: ${bst.right.get.size}")
  }

  def simplePrint: Unit = bst.console
}

object Solution {

  val numbers = List(9, 1, 0, 2, 3, 4, 6, 8, 7, 10, 5)

  def main(args: Array[String]): Unit = {
    val bst = new BinaryTree(numbers)
    bst.printBst
    bst.leftRightSizePrint
  }

}