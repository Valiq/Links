
using BinarySearchTree;

internal class Program
{
    private static void Main(string[] args)
    {
        var tree = new Tree<int>(); //создаём экземпляр дерева

        while (true)
        {
            Console.Clear();
            Console.WriteLine("0 - Формирование двоичного дерева");
            Console.WriteLine("1 - Добавление элемента");
            Console.WriteLine("2 - Префиксный(прямой)  обход");
            Console.WriteLine("3 - Постфиксный(обратный) обход");
            Console.WriteLine("4 - Инфиксный(симметричный) обход");
            Console.WriteLine("5 - Поиск элкмента");
            Console.WriteLine("6 - Удаление элемента\n");

            foreach (var elem in tree.ShowTree())
            {
                Console.WriteLine($"{elem.Data, 3} -> Лев:{elem.Left?.Data, 3} - Прав:{elem.Right?.Data,3}");
            }
            Console.Write("\n-> ");

            try
            {
                switch (int.Parse(Console.ReadLine()))
                {
                    case 0:
                        Console.WriteLine("Введите значение через пробел");
                        int[] mas = Array.ConvertAll(Console.ReadLine().Split(' '), int.Parse);
                        foreach (var elem in mas)
                        {
                            tree.Add(elem);
                        }
                        break;
                    case 1:
                        Console.WriteLine("Введите значение узла");
                        tree.Add(int.Parse(Console.ReadLine() ?? "-1")); //добавление узлов
                        break;
                    case 2:
                        Console.WriteLine("Префиксный(прямой) обход");
                        foreach (var item in tree.Preorder()) // вывод результата префиксного обхода
                        {
                            Console.Write($"{item} ");
                        }
                        Console.ReadLine();
                        break;
                    case 3:
                        Console.WriteLine("\nПостфиксный(обратный) обход");
                        foreach (var item in tree.Postorder()) // вывод результата постфиксного обхода
                        {
                            Console.Write($"{item} ");
                        }
                        Console.ReadLine();
                        break;
                    case 4:
                        Console.WriteLine("\nИнфиксный(симметричный) обход");
                        foreach (var item in tree.Inorder()) // вывод результата инфиксного обхода
                        {
                            Console.Write($"{item} ");
                        }
                        Console.ReadLine();
                        break;
                    case 5:
                        Console.WriteLine("Введите значение для поиска:");
                        Search(int.Parse(Console.ReadLine() ?? "-1"), tree);
                        Console.ReadLine();
                        break;
                    case 6:
                        Console.WriteLine("Введите значение для удаления:");
                        int num = int.Parse(Console.ReadLine() ?? "-1");

                        if (Search(num, tree))
                        {
                            Console.Write(" и удалён");
                            tree.DeleteNode(tree.FindeNode(num));
                        }
                        Console.ReadLine();
                        break;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }
        }
    }

    public static bool Search(object data, Tree<int> tree)
    {
        var result = tree.FindeNode(data);

        if (result is null)
        {
            Console.WriteLine("Элемент не найден");
            return false;
        }
        else
        {
            Console.Write($"Элемент со значением {data} найден");
            return true;
        }
    }
}