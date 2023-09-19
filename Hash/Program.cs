using System.Collections;


internal class Table
{
    int Length;
    Dictionary<int, List<int>> items;

    public Table(int length)
    {
        Length = length;
        items = new Dictionary<int, List<int>>(length);
    }

    //Добавление нового элемента
    public void Add(int item)
    {
        var hash = GetHash(item);
        if (hash < 0 || hash >= Length)
        {
            throw new ArgumentException();
        }

        if (items.ContainsKey(hash) is false)
        {
            items.Add(hash, new List<int>());
        }

        items[hash].Add(item);

    }

    //поиск указанного элемента и возвращение индекса его строки
    public int IndexOf(int item)
    {
        var hash = GetHash(item);
        if (hash < 0 || hash >= Length)
        {
            throw new ArgumentException();
        }

        //for (int i = hash; i < items.Count; i++)
        int counter = 0;
        foreach (KeyValuePair<int, List<int>> val in items) 
        {
            counter++;
            foreach (int i in val.Value)
                if (i == item)
                {
                    return counter;
                }
        }

        return -1;
    }

    //Получение хэшкода по заданному ключу
    public int GetHash(int value)
    {
        var valueLength = Math.Ceiling(Math.Log10(value));
        return Convert.ToInt32((value / ((valueLength - 1) * 10)) % (Length - 1));
    }

    //Вывод хэш-таблицы
    public void Show()
    {
        Console.WriteLine("\nВывод хэш-таблицы");
        int counter = 0;
        foreach(KeyValuePair<int, List<int>> item in items)
        {
            counter++;
            Console.Write($"{counter}. ");
            foreach (var val in item.Value)
                Console.Write($"{val} ");
            Console.WriteLine();
        }
    }
}


internal class Program
{
    private static void Main(string[] args)
    {
        int start = 24000;
        int end = 54000;
        int N = 14;
        int M = 10;

        Random rnd = new Random();
        int[] array = new int[N];

        for (int i = 0; i < array.Length; i++)
        {
            array[i] = rnd.Next(start, end);
        }

        foreach (int i in array)
        {
            Console.Write($"{i} ");
        }
        Console.WriteLine();

        Table hashTable = new Table(M);
        foreach (int i in array)
        {
            hashTable.Add(i);
        }

        hashTable.Show();

        Console.WriteLine("Поиск: " + array[5] + "\n" + "Номер в строке: " + hashTable.IndexOf(array[5]));

        Console.ReadKey();
    }
}