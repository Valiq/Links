internal class Program
{
    class Pacman
    {
        public int x, y;

        public Pacman(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

    }

    class Foe
    {
        public int x, y, exceptDir;

        public Foe(int x, int y, int exceptDir)
        {
            this.x = x;
            this.y = y;
            this.exceptDir = exceptDir; // направление от которого идём
        }
    }

    private static void Main(string[] args)
    {
        Console.CursorVisible = false;

        Console.ForegroundColor = ConsoleColor.DarkMagenta;
        Console.WriteLine("     Собери все пилюли и не попадись зеленым");
        Console.ForegroundColor = ConsoleColor.DarkBlue;
        Console.Write("     Для продолжения нажмите любую клавишу...");
        Console.ReadKey();

    go:
        int winscore;

        char[,] map = ReadMap("map.txt", out winscore);

        ConsoleKeyInfo pressedKey = new ConsoleKeyInfo('w', ConsoleKey.W, false, false, false);

        Pacman pacman = new Pacman(2, 1);
        Foe foe1 = new Foe(38, 7, 0);
        Foe foe2 = new Foe(10, 10, 0);
        Foe foe3 = new Foe(36, 1, 0);

        int score = 0;
        bool gameover = false;

        Console.Clear();

        var taskController = new CancellationTokenSource();
        var token = taskController.Token;

        Task th = Task.Run(() =>
        {
            while (!gameover)
            {
                /* if (token.IsCancellationRequested)
                     break; */

                pressedKey = Console.ReadKey();
            }
        }, token);

        Task th2 = Task.Run(() =>
        {
            while (!gameover)
            {
                /* if (token.IsCancellationRequested)
                     break; */

                FoeMoove(foe1, map, ref gameover);
                FoeMoove(foe2, map, ref gameover);
                FoeMoove(foe3, map, ref gameover);
                Thread.Sleep(300);
            }
        }, token);

        map[pacman.x, pacman.y] = '@';

        while (!gameover)
        {
            drawFrame(map, foe1, foe2, foe3);

            Console.SetCursorPosition(50, 0);
            Console.Write($"Score: {score}");

            HandleInput(pressedKey, pacman, map, ref score, ref gameover);

            Thread.Sleep(300);

        }

        taskController.Cancel();

        drawFrame(map, foe1, foe2, foe3);

        Console.SetCursorPosition(50, 0);
        if (score != winscore)
            Console.WriteLine("     ! GAME OVER !");
        else
        {
            Console.ForegroundColor = ConsoleColor.Yellow;
            Console.WriteLine("    ! YOU WIN !");
        }

        Console.SetCursorPosition(50, 1);
        Console.WriteLine("Нажмите два раза Enter, чтобы начать сначала...");
        Console.ReadKey();
        goto go;
    }

    static char[,] ReadMap(string path, out int winscore)
    {
        winscore = 0;
        string[] file = File.ReadAllLines("map.txt");

        char[,] map = new char[file[0].Length, file.Length];

        for (int x = 0; x < map.GetLength(0); x++)
            for (int y = 0; y < map.GetLength(1); y++)
            {
                map[x, y] = file[y][x];
                if (map[x, y] == '.')
                    winscore++;
            }

        return map;
    }

    static void DrawMap(char[,] map)
    {
        for (int y = 0; y < map.GetLength(1); y++)
        {
            for (int x = 0; x < map.GetLength(0); x++)
            {
                if (map[x, y] == '#')
                    Console.ForegroundColor = ConsoleColor.Blue;
                if (map[x, y] == '.')
                    Console.ForegroundColor = ConsoleColor.Magenta;
                if (map[x, y] == '@')
                    Console.ForegroundColor = ConsoleColor.Yellow;
                Console.Write(map[x, y]);
            }

            Console.WriteLine();
        }
    }

    static void drawFrame(char[,] map, Foe foe1, Foe foe2, Foe foe3)
    {
        Console.SetCursorPosition(0, 0);
        DrawMap(map);
        Console.ForegroundColor = ConsoleColor.Green;
        Console.SetCursorPosition(foe1.x, foe1.y);
        Console.Write('$');

        Console.SetCursorPosition(foe2.x, foe2.y);
        Console.Write('$');

        Console.SetCursorPosition(foe3.x, foe3.y);
        Console.Write('$');
        Console.ForegroundColor = ConsoleColor.Red;
    }

    enum dirs
    {
        Up = 1,
        Down = 2,
        Left = 3,
        Right = 4
    };

    static void FoeMoove(Foe obj, char[,] map, ref bool gameover)
    {
        List<int[]> nextDirs = new List<int[]>();

        Type type = typeof(dirs);
        Array val = type.GetEnumValues();

        foreach (int dir in val)
        {
            int[] direction = { 0, 0, 0, 0 };
            switch (dir)
            {
                case 1:
                    direction[1] = -1;
                    direction[3] = 2; // индекс взаимоисключающего направления
                    break;
                case 2:
                    direction[1] = 1;
                    direction[3] = 1;
                    break;
                case 3:
                    direction[0] = -1;
                    direction[3] = 4;
                    break;
                case 4:
                    direction[0] = 1;
                    direction[3] = 3;
                    break;
            }

            direction[2] = dir; //индекс напрпаления
            int nextFoePositionX = obj.x + direction[0];
            int nextFoePositionY = obj.y + direction[1];

            char nextCell = map[nextFoePositionX, nextFoePositionY];

            if (nextCell == '@')
            {
                gameover = true;
                return;
            }

            if (nextCell == ' ' || nextCell == '.')
                nextDirs.Add(direction);
        }

        //Console.WriteLine($"старое {obj.exceptDir}");
        if (nextDirs.Count > 1)
            foreach (var row in nextDirs.ToList())
                if (row[2] == obj.exceptDir)
                    nextDirs.RemoveAt(nextDirs.IndexOf(row));


        Random random = new Random();

        int newDir = random.Next(nextDirs.Count);

        obj.x = obj.x + nextDirs[newDir][0];
        obj.y = obj.y + nextDirs[newDir][1];

        obj.exceptDir = nextDirs[newDir][3];

        if (map[obj.x, obj.y] == '@')
        {
            gameover = true;
            return;
        }

        foreach (int dir in val)
        {
            int[] direction = { 0, 0 };
            switch (dir)
            {
                case 1:
                    direction[1] = -1;
                    break;
                case 2:
                    direction[1] = 1;
                    break;
                case 3:
                    direction[0] = -1;
                    break;
                case 4:
                    direction[0] = 1;
                    break;
            }

            int nextFoePositionX = obj.x + direction[0];
            int nextFoePositionY = obj.y + direction[1];

            char nextCell = map[nextFoePositionX, nextFoePositionY];

            if (nextCell == '@')
                gameover = true;
        }
    }

    static void HandleInput(ConsoleKeyInfo pressedKey, Pacman obj, char[,] map, ref int score, ref bool gameover)
    {
        int[] direction = GetDirection(pressedKey);

        int nextPacmanPositionX = obj.x + direction[0];
        int nextPacmanPositionY = obj.y + direction[1];

        char nextCell = map[nextPacmanPositionX, nextPacmanPositionY];

        if (nextCell == ' ' || nextCell == '.')
        {
            int prevPosX = obj.x;
            int prevPosY = obj.y;

            obj.x = nextPacmanPositionX;
            obj.y = nextPacmanPositionY;

            if (nextCell == '.')
                score++;

            map[nextPacmanPositionX, nextPacmanPositionY] = '@';
            map[prevPosX, prevPosY] = ' ';
        }
    }

    static int[] GetDirection(ConsoleKeyInfo pressedKey)
    {
        int[] direction = { 0, 0 };

        switch (pressedKey.Key)
        {
            case ConsoleKey.UpArrow:
                direction[1] = -1;
                break;
            case ConsoleKey.DownArrow:
                direction[1] = 1;
                break;
            case ConsoleKey.LeftArrow:
                direction[0] = -1;
                break;
            case ConsoleKey.RightArrow:
                direction[0] = 1;
                break;
        }

        return direction;
    }

}


