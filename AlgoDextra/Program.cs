internal class Program
{
    public static void Dex(int[,] matrix, int start)
    {
        int[] minDist = new int[matrix.GetLength(0)];
        int[] visit = new int[matrix.GetLength(0)];

        for (int i = 0; i < matrix.GetLength(0); i++)
        {
            minDist[i] = int.MaxValue;
        }

        minDist[start] = 0;

        for (int i = 0; i < matrix.GetLength(0); i++)
        {
            int minId = mDist(minDist, visit);
            visit[minId] = 1;

            for (int j = 0; j < matrix.GetLength(0); j++)
                if (visit[j] == 0 && matrix[minId, j] != 0)
                    if (minDist[minId] != int.MaxValue && minDist[minId] + matrix[minId, j] < minDist[j])
                        minDist[j] = minDist[minId] + matrix[minId, j];                 
        }

        Console.WriteLine("Вершина\tКратчайшее расстояние");

        for (int i = 0; i < matrix.GetLength(0); i++)
        {
            Console.WriteLine($"{i}\t{minDist[i]}");
        }
    }

    public static int mDist(int[] minDist, int[] visit)
    {
        int min = int.MaxValue;
        int minId = 0;

        for(int i = 0; i < minDist.Length; i++) 
        {
            if (visit[i] == 0 && minDist[i] <= min)
            {
                min = minDist[i];
                minId = i;
            }
        }

        return minId;
    }
   
    static void Main(string[] args)
    {
        int[,] matrix =  {
                            {0,3,34,3,6,56,34},
                            {3,0,23,67,9,4,8},
                            {34,23,0,5,80,2,59},
                            {3,67,5,0,3,89,27},
                            {6,9,80,3,0,4,33},
                            {56,4,2,89,4,0,47},
                            {34,8,59,27,33,47,0}
        };

        Dex(matrix, 0);
        Console.ReadKey();
    }
}