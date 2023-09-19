using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BinarySearchTree
{
    internal class Node<T> //класс узла дерева
        where T : IComparable //данный интерфейс позволяет производить сравнения (>,<,=)
    {
        public T Data { get; set; } //данные, которые будут находиться в узле
        public Node<T> Left { get; set; } //потомки конкретного узла (поддерево слева)
        public Node<T> Right { get; set; } //потомки конкретного узла (поддерево справа)
        public Node<T> Parent { get; set; } //родитель конкретного узла

        public Node(T data) //конструктор класса
        {
            this.Data = data;
        }

        public Node(T data, Node<T> left, Node<T> right) //конструктор класса
        { 
            this.Data = data;
            this.Left = left;
            this.Right = right;
        }

        public void Add(T data, Node<T> parent) //добавление нового корня со значением Т(обобщенным)
        {
            Node<T> node = new Node<T>(data); 
            node.Parent = parent;

            if(node.Data.CompareTo(Data) == -1) //проверяем на больше или меньше для дальнейшего направления
            {
                if(Left == null) //проверка на наличие левого элемента
                {
                    Left = node; //если отсутствует - добавляем
                }
                else
                {
                    Left.Add(data, Left); //если присутствует ссылка на подвершино, то осуществляется рекурсивный переход
                }
            }
            else if (node.Data.CompareTo(Data) == 1)
            {
                if(Right == null)   //проверка на наличие правого элемента
                {
                    Right = node; //если отсутствует - добавляем
                }
                else
                { 
                    Right.Add(data, Right); // если присутствует ссылка на подвершино, то осуществляется рекурсивный переход
                }
            }
        }

        public int CompareTo(object obj) //сравнение объекта со значением узла дерева
        {
            if(obj is Node<T> item) //если obj удалось привести к типу Node<T>, то значение будет помещено в item
            {
                return Data.CompareTo(item); //сравнение значений Data и item
            }
            else //в противном случае будет выдана ошибка 
            {
                throw new Exception("Не совпадение типов");
            }
        }

        public int CompareTo(T other) //сравнение значений между узлами
        {
            return Data.CompareTo(other);
        }

        public override string ToString()
        {
            return Data.ToString() ?? "";
        }
    }
}
