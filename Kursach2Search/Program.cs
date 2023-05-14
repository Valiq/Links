internal class Program
{
    static int findPositionIter(int[] numlist, int numb)
    {
        int first = 0;
        int last = numlist.Length-1;
        Array.Sort(numlist);

        while (true)
        {
            int middle = (first + last)/2;

            if (numlist[middle] == numb)
                return middle;
            
            if (first == last)
                return -1;

            if (numb < numlist[middle])
            {
                last = middle;
            }
            else
            {
                first = middle + 1;
            }
        }
    }

    static void findPositionRecur(int[] numlist, int number)
    {
        int first = 0;
        int last = numlist.Length - 1;
        Array.Sort(numlist);

        int result = BinarySearch(numlist, number, first, last);

        if (result != -1)
        {
            Console.WriteLine($"Элемент найден на позиции {result+1}");
        }
        else
        {
            Console.WriteLine($"Элемент не найден");
        }
    }

    static int BinarySearch(int[] numlist, int numb, int first, int last)
    {
        if (first == last)
            return -1;

        int middle = (first + last) / 2;

        if (numlist[middle] == numb)
        { 
            return middle;
        }
        else
        {
            if (numb < numlist[middle])
            {
                return BinarySearch(numlist, numb, first, middle);
            }
            else
            {
                return BinarySearch(numlist, numb, middle + 1, last);
            }
        }
    }

    private static void Main(string[] args)
    {
        go:
        try
        {
            Console.WriteLine("Введите элементы массива через пробел");

            int[] list = Array.ConvertAll(Console.ReadLine().Split(' '),int.Parse);

            Console.WriteLine("Введите искомое число");
            int number = int.Parse(Console.ReadLine() ?? "-1");

            int result = findPositionIter(list, number);

            Console.WriteLine("Отсортированный массив: ");
            for (int i = 0; i < list.Length; i++)
                Console.Write($"{list[i]} ");

            Console.WriteLine("\n\nИтеративная реализация алгоритма:");

            if (result != -1)
            {
                Console.WriteLine($"Элемент найден на позиции {result+1}");
            }
            else
            {
                Console.WriteLine($"Элемент не найден");
            }

            Console.WriteLine("\nРекурсивная реализация алгоритма:");

            findPositionRecur(list,number);
        } 
        catch (Exception e)
        {
            Console.WriteLine (e);
            Console.ReadLine();
            Console.Clear();
            goto go;
        }
    }
}