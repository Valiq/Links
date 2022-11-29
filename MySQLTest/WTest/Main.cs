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
using System.Diagnostics;

namespace WTest
{
	public partial class Main : Form
	{
		Library lib = new Library();

		public Main()
		{
			InitializeComponent();
		}

		private void toolStripButton1_Click(object sender, EventArgs e)
		{
			EditAdd ea = new EditAdd();
			ea.Owner = this;
			ea.flag = false;
			ea.textBox3.Text = 0.ToString();
			//ea.textBox3.ReadOnly = true;
			ea.button3.Hide();
			ea.button2.Show();
			ea.label7.Text = "Добавление";
			ea.Show();
		}

		private void Main_Load(object sender, EventArgs e)
		{
			FillDataGrid();
		}

		private void toolStripButton2_Click(object sender, EventArgs e)
		{
			EditAdd ea = new EditAdd();
			ea.Owner = this;
			ea.flag = true;
			ea.IdUrl = Convert.ToInt32(dataGridView1.CurrentRow.Cells[0].Value);
			ea.button2.Hide();
			ea.button3.Show();
			ea.label7.Text = "Изменение";
			ea.Show();
		}

		private void toolStripButton3_Click(object sender, EventArgs e)
		{
			DialogResult result = MessageBox.Show("Данные будут безвозвратно удалены. Продолжить ?", "Подтверждение удаления", MessageBoxButtons.YesNo);
			if (result == DialogResult.Yes)
			{
				foreach (DataGridViewRow row in dataGridView1.SelectedRows)
				{
					int Id = Convert.ToInt32(row.Cells[0].Value);
					lib.DeleteUrl(Id, "url");
				}
				FillDataGrid();
			}	
		}

		public void FillDataGrid()
		{
			dataGridView1.Rows.Clear();
			var links = lib.GetAllUrl();
			if (links != null)
			{
				foreach (url link in links)
				{
					dataGridView1.Rows.Add(link.Id, link.LongUrl, link.ShortUrl, link.Date.ToString("dd.MM.yyyy"), link.Count);
				}
			}
		}

		private void dataGridView1_SelectionChanged(object sender, EventArgs e)
		{
			if (dataGridView1.RowCount != 0) 
				dataGridView1.CurrentRow.Selected = true;
		}

		private void toolStripButton4_Click(object sender, EventArgs e)
		{
			FillDataGrid();
		}

		private void dataGridView1_DoubleClick(object sender, EventArgs e)
		{
			try
			{
				Process.Start(dataGridView1.CurrentRow.Cells[1].Value.ToString());
				url url = new url();

				url.Id = Convert.ToInt32(dataGridView1.CurrentRow.Cells[0].Value);
				url.LongUrl = dataGridView1.CurrentRow.Cells[1].Value.ToString();
				url.ShortUrl = dataGridView1.CurrentRow.Cells[2].Value.ToString();
				url.Date = Convert.ToDateTime(dataGridView1.CurrentRow.Cells[3].Value);
				url.Count = Convert.ToInt32(dataGridView1.CurrentRow.Cells[4].Value) + 1;

				lib.UpdateUrl(url);
				FillDataGrid();
			}
			catch (Exception ex)
			{
				MessageBox.Show(ex.Message);
			}
		}
	}
}
