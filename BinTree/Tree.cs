using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection.Metadata;
using System.Text;
using System.Threading.Tasks;

namespace BinarySearchTree
{
    internal class Tree<T> //клас бинарного дерева
        where T : IComparable //данный интерфейс позволяет производить сравнения (>,<,=)
    {
        public Node<T> Root { get; set; } //корень дерева
        public int Count { get; set; } //количество корней в дереве

        public void Add(T data) //добавление нового корня со значением Т(обобщенным)
        {
            if(Root == null) //если корневой элемент отсутствует, присваеваем пустой
            {
                Root = new Node<T>(data);
                Root.Parent = null;
                Count = 1;
                return;
            }

            Root.Add(data, Root); //добавление нового элемента
            Count++;    //увеличивем количество элементов на единицу
        }

        public List<Node<T>> ShowTree()
        {
            if (Root == null)
            {
                return new List<Node<T>>();
            }

            return ShowTree(Root);
        }

        public List<Node<T>> ShowTree(Node<T> node)
        {
            var nodeList = new List<Node<T>>();

            if (node != null)
            {
                nodeList.Add(node);  //добавление в список посещенных элементов

                if (node.Left != null)
                {
                    nodeList.AddRange(ShowTree(node.Left)); //если левый потомок существует, то переход к нему через рекурсию
                }

                if (node.Right != null)
                {
                    nodeList.AddRange(ShowTree(node.Right)); //если правый потомок существует, то переход к нему через рекурсию
                }
            }

            return nodeList;
        }

        //(публичные обертки)
        public List<T> Preorder() //префиксный обход элементов дерева и вывод их в качестве списка
        {
            if (Root == null)   //если отсутствует корневой элемент, то возвращается пустой список
            {
                return new List<T>();
            }
            
            return  Preorder(Root); //рекурсивный вызов
        }

        public List<T> Postorder() //постфиксный обход элементов дерева и вывод их в качестве списка
        {
            if (Root == null)   //если отсутствует корневой элемент, то возвращается пустой список
            {
                return new List<T>();
            }

            return Postorder(Root); //рекурсивный вызов
        }

        public List<T> Inorder() //инфиксный обход элементов дерева и вывод их в качестве списка
        {
            if (Root == null)   //если отсутствует корневой элемент, то возвращается пустой список
            {
                return new List<T>();
            }

            return Inorder(Root); //рекурсивный вызов
        }

        public List<T> Preorder(Node<T> node) //префиксный обход - сначала обрабатывается текущий узел, затем левое и правое поддеревья
        {
            var list = new List<T>();   // создание списка

            if (node != null) 
            {
                list.Add(node.Data);  //добавление в список посещенных элементов

                if(node.Left != null)
                {
                    list.AddRange(Preorder(node.Left)); //если левый потомок существует, то переход к нему через рекурсию
                }

                if (node.Right != null)
                {
                    list.AddRange(Preorder(node.Right)); //если правый потомок существует, то переход к нему через рекурсию
                }
            }

            return list;    //в случае достижение последнего элемента без потомков, возвращается список

        }

        public List<T> Postorder(Node<T> node) //постфиксный обход сначала обрабатываются левое и правое поддеревья текущего узла, затем сам узел
        {
            var list = new List<T>();   // создание списка

            if (node != null)
            {
                if (node.Left != null)
                {
                    list.AddRange(Postorder(node.Left)); //если левый потомок существует, то переход к нему через рекурсию
                }

                if (node.Right != null)
                {
                    list.AddRange(Postorder(node.Right)); //если правый потомок существует, то переход к нему через рекурсию
                }

                list.Add(node.Data);  //добавление в список посещенных элементов
            }

            return list;    //в случае достижение последнего элемента без потомков, возвращается список

        }

        public List<T> Inorder(Node<T> node) //инфиксный обход - сначала обрабатывается левое поддерево текущего узла, затем корень, затем правое поддерево
        {
            var list = new List<T>();   // создание списка

            if (node != null)
            {
                if (node.Left != null)
                {
                    list.AddRange(Inorder(node.Left)); //если левый потомок существует, то переход к нему через рекурсию
                }
                
                list.Add(node.Data);  //добавление в список посещенных элементов

                if (node.Right != null)
                {
                    list.AddRange(Inorder(node.Right)); //если правый потомок существует, то переход к нему через рекурсию
                }
            }

            return list;    //в случае достижение последнего элемента без потомков, возвращается список

        }

        public Node<T>? FindeNode(object data, Node<T>? node = null, int count = 0) //метод для поиска элементов
        {
            if (count == this.Count)   //если вершины все пройден и элемент не найден - выход из метода
                return null;

            count++;    //число пройденных вершин

            node = node ?? Root;    // проверка на null стартового элемента

            if (node.Data.CompareTo(data) == 0) 
            {
                return node;   // возвращаем значение 
            }

            if (node.Data.CompareTo(data) < 0)
            {
                return FindeNode(data, node.Right, count); //рекурсивный проход в правое поддерево
            }
            else
            {
                return FindeNode(data, node.Left, count); //рекурсивный проход в левое поддерево
            }
        }

        public void DeleteNode(Node<T> node)
        {
            if (node == null)
            {
                return;
            }

            var parent = node.Parent;

            if (node.Right is null && node.Left is null)  //если у узла нет подузлов, можно его удалить
            {
                if (parent.Left == node)
                {
                    node.Parent.Left = null;
                }
                else
                {
                    node.Parent.Right = null;
                }
            }
            else if (node.Left is null)  //если нет левого, то правый ставим на место удаляемого 
            {
                if (parent.Left == node)
                {
                    parent.Left = node.Right;
                }
                else
                {
                    parent.Right = node.Right;
                }
                node.Right.Parent = parent;
            }
            else if (node.Right is null)  //если нет правого, то левый ставим на место удаляемого 
            {
                if (parent.Left == node)
                {
                    parent.Left = node.Left;
                }
                else
                {
                    parent.Right = node.Left;
                }
                node.Left.Parent = parent;
            }
            else                             //если оба дочерних присутствуют, то правый становится на место удаляемого,                                          
            {                                //а левый вставляется в правый
                parent.Left = node.Right;
                node.Right.Parent = parent;
                node.Right.Add(node.Left.Data, node.Right);
            }
        }
    }
}
