using System.Text.RegularExpressions;

namespace Program
{
    class Program
    {
        static void View (FileStream file, List<String[]> list){
            String[] row = new String[7];

            StreamReader reader = new StreamReader(file);

            string? line;
            while ((line =  reader.ReadLine()) != null){
                row = line.Split("|",StringSplitOptions.RemoveEmptyEntries);
                list.Add(row);
            }

            reader.Close();
        }
        
        static void Show (List<String[]> list){
            Console.WriteLine("  id\t|\t Фамилия Имя Отчество\t\t|\t Группа\t|    Физика\t|  Математика\t|  Информатика\t|   Последний экзамен\t|\t");
            Console.WriteLine("\t---------------------------------------------------------------------------------------------------------");
            foreach(var row in list){
                Console.Write("  ");

                foreach(var s in row )
                    Console.Write($"{s}\t|\t");

                Console.WriteLine();
            }
        }

        static void Search(List<String[]> list){
            start:
            Menu();
            Console.WriteLine("\n Выберите критерии для поиска (через пробел)");
            Console.WriteLine("\t1 - ФИО");
            Console.WriteLine("\t2 - Номер группы");
            Console.WriteLine("\t3 - Оценка по Физике");
            Console.WriteLine("\t4 - Оценка по Математике");
            Console.WriteLine("\t5 - Оценка по Информатике");
            Console.WriteLine("\t6 - Последний экзамен");
            Console.WriteLine("\t7 - По всем критериям");
            Console.WriteLine("\t0 - Назад");
            Console.Write("Выбор номера: ");

            string str =  Console.ReadLine() ?? "";
            string[] mas = str.Split(new char[]{',',' '},StringSplitOptions.RemoveEmptyEntries);

            if (mas[0] == "7") mas = "1 2 3 4 5 6".Split(' ');

            List<String[]> result = list;

            Menu();
            string name="", group="", phis="", mat="", inf="", exam="";
            foreach (var n in mas)
                switch (int.Parse(n)){
                    case 1:
                        Console.Write("Введите ФИО: ");
                        name = Console.ReadLine() ?? "";
                        var regex_name = new Regex(name);
                        result = result.Where(row => regex_name.IsMatch(row[1])).ToList();
                    break; 
                    case 2:
                        Console.Write("Введите номер группы: ");
                        group = Console.ReadLine() ?? "";
                        result = result.Where(row => row[2] == group).ToList();
                    break;
                    case 3:
                        Console.Write("Введите оценку по Физике: ");
                        phis = Console.ReadLine() ?? "";
                        result = result.Where(row => Int32.Parse(row[3]) == Int32.Parse(phis)).ToList();
                    break;
                    case 4:
                        Console.Write("Введите оценку по Матиматике: ");
                        mat = Console.ReadLine() ?? ""; 
                        result = result.Where(row => Int32.Parse(row[4]) == Int32.Parse(mat)).ToList();
                    break;
                    case 5:
                        Console.Write("Введите оценку пр Информатике:");
                        inf = Console.ReadLine() ?? "";
                        result = result.Where(row => Int32.Parse(row[5]) == Int32.Parse(inf)).ToList();
                    break;
                    case 6:
                        Console.Write("Введите дату последнего экзамена: ");
                        exam = Console.ReadLine() ?? "";
                        var regex_exam = new Regex(exam);
                        result = result.Where(row => regex_exam.IsMatch(row[6])).ToList();
                    break;
                    case 0:
                    return;
            }

            Show(result);
            Console.ReadLine();
            goto start;

            /* List<String[]> result = list.Where(row => 
                regex_name.IsMatch(row[1]) && 
                row[2] == group && row[3] == phis && row[4] == mat && row[5] == inf &&
                regex_exam.IsMatch(row[6])).ToList(); */           
        }

        static void Add(List<String[]> list, FileStream file){
            String[] row = new String[7];

            Console.WriteLine("  Добавление нового элемента:\n");

            row[0] = (MaxId(list)+1).ToString();

            Console.Write("  Введите ФИО:  ");
            row[1] = Console.ReadLine() ?? "";
            Console.Write("  Введите номер группы:  ");
            row[2] = Console.ReadLine() ?? "";
            Console.Write("  Введите оценку по Физике:  ");
            row[3] = Console.ReadLine() ?? "";
            Console.Write("  Введите оценку по Математике:  ");
            row[4] = Console.ReadLine() ?? "";
            Console.Write("  Введите оценку по Информатике:  ");
            row[5] = Console.ReadLine() ?? "";
            Console.Write("  Введите дату последнего экзамена:  ");
            row[6] = Console.ReadLine() ?? "";

            list.Add(row);

            FileWrite(list, file, false);
        }

        static void Update(List<String[]> list, string id, FileStream file){
            bool flag = false;

            foreach (var row in list.ToList())
                if (int.Parse(row[0]) == int.Parse(id)){
                    Console.WriteLine($"Введите новое ФИО (старое \"{row[1]}\"):");
                    string s = string.Format(Console.ReadLine() ?? "",StringSplitOptions.RemoveEmptyEntries);
                    if (s != "") {
                        row[1] = s;
                    }

                    Console.WriteLine($"Введите новую группу (старое \"{row[2]}\")");
                    s = string.Format(Console.ReadLine() ?? "",StringSplitOptions.RemoveEmptyEntries);
                    if (s != "") {
                        row[2] = s;
                    }

                    Console.WriteLine($"Введите новую оценку по Физике (старое \"{row[3]}\")");
                    s = string.Format(Console.ReadLine() ?? "",StringSplitOptions.RemoveEmptyEntries);
                    if (s != "") {
                        row[3] = s;
                    }

                    Console.WriteLine($"Введите новую оценку по Математике (старое \"{row[4]}\")");
                    s = string.Format(Console.ReadLine() ?? "",StringSplitOptions.RemoveEmptyEntries);
                    if (s != "") {
                        row[4] = s;
                    }

                    Console.WriteLine($"Введите новую оценку по Информатике (старое \"{row[5]}\")");
                    s = string.Format(Console.ReadLine() ?? "",StringSplitOptions.RemoveEmptyEntries);
                    if (s != "") {
                        row[5] = s;
                    }

                    Console.WriteLine($"Введите новую дату последнего эказамена (старое \"{row[6]}\")");
                    s = string.Format(Console.ReadLine() ?? "",StringSplitOptions.RemoveEmptyEntries);
                    if (s != "") {
                        row[6] = s;
                    }

                    flag = true;
                }

            if (flag == false){
                Console.WriteLine($"Объект с id = {id} не найден"); 
                return;
            }

            FileWrite(list, file, false);            
        }

        static void Delete(List<String[]> list, string[] masId, FileStream file){
            foreach (var id in masId){
                bool flag = false;

                foreach (var row in list.ToList())
                    if (int.Parse(row[0]) == int.Parse(id)){
                        //Console.WriteLine(list.IndexOf(row));
                        list.RemoveAt(list.IndexOf(row));   
                        flag = true;
                    }
                
                if (flag == false){
                    Console.WriteLine($"Объект с id = {id} не найден"); 
                    return;
                }
            }
            
            FileWrite(list, file, false);
        }

        static void Last(List<String[]> list){
            start:
            Menu();
            Console.Write("Введите дату окончания сессии: ");
            string data = Console.ReadLine() ?? "";
            try{
                DateTime.Parse(data);
            } catch {
                Console.ForegroundColor = ConsoleColor.DarkRed;
                Console.WriteLine("Неверный формат ввода даты (требуется dd/mm/yyyy)");
                Console.ResetColor();
                Console.ReadKey();
                goto start;
            }

            var result = list.Where(row => DateTime.Parse(row[6]) < DateTime.Parse(data)).
                              GroupBy(row => row[2]).Select(x => new {a = x.Key, 
                              b = x.Select(g => new {name = g[1], ball = (Int32.Parse(g[3]) + Int32.Parse(g[4]) + Int32.Parse(g[5]))/3}).OrderByDescending(row => row.ball)});
                              //SelectMany(g => g, (l,d) => new {name = d[1], ball = (Int32.Parse(d[3]) + Int32.Parse(d[4]) + Int32.Parse(d[5]))/3})

            if (result.Count() == 0)
                Console.WriteLine(" Совпадений не найдено");

           foreach(var grup in result){
                Console.WriteLine($"\n Группа {grup.a}");
                foreach(var chel in grup.b){
                    Console.WriteLine($"   {chel.name}\t средний балл ({chel.ball})");
                }
           }

           Console.WriteLine("\n 1 - Продолжить поиск");
           Console.WriteLine(" 2 - Сохранить в файл");
           Console.WriteLine(" 0 - Назад");
           Console.Write("Выберете действие -> ");
           int i = Int32.Parse(Console.ReadLine() ?? "");

           switch (i){
                case 1:
                    goto start;
                case 2:
                    Console.Write("\nВведите название файла: ");
                    string name = Console.ReadLine() ?? "default";

                    StreamWriter writer = new StreamWriter(name);

                    foreach(var grup in result){
                        writer.Write($" Группа {grup.a}\n");
                        foreach(var chel in grup.b){
                            writer.Write($"    {chel.name}\t средний балл ({chel.ball})\n");
                        }
                        writer.Write('\n');
                    }

                    writer.Close();

                    Console.ForegroundColor = ConsoleColor.Green;
                    Console.WriteLine("Операция выполнена успешна");
                    Console.ResetColor();

                    break;
                case 3:
                    return;
           }            
        }

        static void FileWrite(List<String[]> list, FileStream file, bool flag){
            StreamWriter writer = new StreamWriter(file.Name,flag);

            foreach (var row in list) {
                foreach (var s in row)
                    writer.Write($"{s}|");
                writer.Write('\n');
            }

            writer.Close();

            Console.ForegroundColor = ConsoleColor.Green;
            Console.WriteLine("Операция выполнена успешна");
            Console.ResetColor();
        }

        static int MaxId(List<String[]> list){
            int max = 0;
            foreach (var row in list){
                int buf = 0;
                try {
                    buf = int.Parse(row[0]);
                } catch {}
                if (buf > max)
                    max = buf;
            }
            
            return max;
        }
        
        static void Menu(){
            Console.Clear();
            Console.WriteLine("  Выберите действие:");
            Console.WriteLine("  1. Вывести весь список");
            Console.WriteLine("  2. Поиск");
            Console.WriteLine("  3. Добавить");
            Console.WriteLine("  4. Изменить");
            Console.WriteLine("  5. Удалить");
            Console.WriteLine("  6. Список студентов сдавших последний экзамен до даты окончания сессии");
            Console.WriteLine("  0. Выход"); 
        }

        static int Read(){
            read:
            int k;
            Console.Write("Ввод: ");
            try {
                k = int.Parse(Console.ReadLine() ?? "-1");
            } catch {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Ошибка: неверный формат");
                Console.ResetColor();
                Console.ReadKey();
                goto read;
            }
            
            return k;
        }


        static void Main(string[] args)
        {
            List<String[]> list = new List<String[]>();

            FileStream file = new FileStream("file.txt", FileMode.OpenOrCreate);

            View(file,list);

            Menu();
            
            read:
            switch (Read()) {
                case 1:
                    Menu();
                    Show(list);
                    goto read;
                case 2:
                    Search(list);
                    goto read;
                case 3:
                    Menu();
                    Add(list,file);
                    goto read;
                case 4:
                    goid4:
                    Menu();
                    Console.Write("Введите id записи для измения: ");
                    string id = (Console.ReadLine() ?? "");

                    bool flag = true; int i = 0;
                    flag = int.TryParse(id, out i);

                    if (flag == true){
                        Update(list,id,file);
                    } else {
                        Console.ForegroundColor = ConsoleColor.Red;
                        Console.WriteLine("  Некорректный ввод");
                        Console.ResetColor();
                        goto goid4;
                    }
                    goto read;
                case 5:
                    goid5:
                    Console.WriteLine("  Введите id записи для удаления (для нескольких записей ввод через запятую)");
                    string str = (Console.ReadLine()  ?? "-1");

                    string[] masId = str.Split(new char[]{',',' '},StringSplitOptions.RemoveEmptyEntries);

                    flag = true; i = 0;
                    foreach(var s in masId){
                        flag = int.TryParse(s,out i);
                    }

                    if (flag == true){
                        Delete(list,masId,file);
                    } else {
                            Console.ForegroundColor = ConsoleColor.Red;
                            Console.WriteLine("  Некорректный ввод");
                            Console.ResetColor();
                            goto goid5;
                    }
                    goto read;
                case 6:
                    Last(list);
                    goto read;
                case 0:
                    goto end;
                default:
                    goto read;
            }
            
            end:
            Console.ForegroundColor = ConsoleColor.DarkMagenta;
            Console.WriteLine("Programm end");
            Console.ResetColor();
        }
    }
}