using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Model;
using Lib;

namespace WTest
{
	public partial class EditAdd : Form
	{

		public bool flag { get; set; }
		Library lib = new Library();
		public int IdUrl;

		public EditAdd()
		{
			InitializeComponent();
		}

		private void button1_Click(object sender, EventArgs e)
		{
			string str = "";
			var rand = new Random();
			for (int i=0; i<5; i++)
			{
				int n = rand.Next(3);
				if (n == 0)
				{
					str = str + rand.Next(10).ToString();
				}
				if (n == 1)
				{
					str = str + (char)rand.Next(65,90);
				}
				if (n == 2)
				{
					str = str + (char)rand.Next(97, 122);
				}
			}
			textBox2.Text = str;
		}

		private void EditAdd_Load(object sender, EventArgs e)
		{
			if (flag)
			{

				url url = lib.GetUrlById(IdUrl).ToList()[0];

				textBox1.Text = url.LongUrl;
				textBox2.Text = url.ShortUrl;
				maskedTextBox1.Text = url.Date.ToString("dd.MM.yyyy");
				textBox3.Text = url.Count.ToString();

			}
		}

		private void button2_Click(object sender, EventArgs e)
		{
			try
			{
				url url = new url();

				url.LongUrl = textBox1.Text;
				url.ShortUrl = textBox2.Text;
				url.Date = Convert.ToDateTime(maskedTextBox1.Text);
				url.Count = Convert.ToInt32(textBox3.Text);
				
				lib.AddUrl(url);
				Close();
			}
			catch (Exception ex)
			{
				MessageBox.Show(ex.Message);
			}
		}

		private void dateTimePicker1_ValueChanged(object sender, EventArgs e)
		{
			maskedTextBox1.Text = dateTimePicker1.Value.ToString("dd.MM.yyyy");
		}

		private void button3_Click(object sender, EventArgs e)
		{
			try
			{
				url url = new url();

				url.Id = IdUrl;
				url.LongUrl = textBox1.Text;
				url.ShortUrl = textBox2.Text;
				url.Date = Convert.ToDateTime(maskedTextBox1.Text);
				url.Count = Convert.ToInt32(textBox3.Text);

				lib.UpdateUrl(url);
				Close();
			}
			catch (Exception ex)
			{
				MessageBox.Show(ex.Message);
			}
		}
	}
}
