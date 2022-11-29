using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.IO;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Model;
using MySql.Data.MySqlClient;
using Dapper;
using System.Text.RegularExpressions;


namespace Lib
{
	public class Library
	{

		public IEnumerable<url> GetAllUrl()
		{
			try
			{
				string sql = "SELECT * FROM url";
				using (IDbConnection con = new MySqlConnection(ConfigurationManager.ConnectionStrings["con"].ConnectionString))
				{
					con.Open();
					var result = con.Query<url>(sql);
					return result;
				}
			}
			catch (Exception ex)
			{
				MessageBox.Show(ex.Message);
			}
			return null;
		}

		public void DeleteUrl(int id, string table)
		{
			try
			{
				string sql = "DELETE FROM " + table + " WHERE Id = @Id";
				using (IDbConnection con = new MySqlConnection(ConfigurationManager.ConnectionStrings["con"].ConnectionString))
				{
					con.Open();
					con.Execute(sql, new url { Id = id });
				}
			}
			catch (Exception ex)
			{
				MessageBox.Show(ex.Message);
			}
		}

		public void UpdateUrl(url url)
		{
			try
			{
				string sql = @"UPDATE url SET LongUrl = @LongUrl, ShortUrl = @ShortUrl, Date = @Date, Count = @Count WHERE Id = @Id ";
				using (IDbConnection con = new MySqlConnection(ConfigurationManager.ConnectionStrings["con"].ConnectionString))
				{
					con.Open();
					var result = con.Execute(sql, url);
				}
			}
			catch (Exception ex)
			{
				MessageBox.Show(ex.Message);
			}
		}

		public void AddUrl(url url)
		{
			try
			{
				string sql = @"INSERT INTO url (LongUrl,ShortUrl,Date,Count)
                                                VALUES (@LongUrl,@ShortUrl,@Date,@Count)";
				using (IDbConnection con = new MySqlConnection(ConfigurationManager.ConnectionStrings["con"].ConnectionString))
				{
					con.Open();
					var result = con.Execute(sql, url);
				}
			}
			catch (Exception ex)
			{
				MessageBox.Show(ex.Message);
			}
		}

		public IEnumerable<url> GetUrlById(int id)
		{
			try
			{
				string sql = "SELECT * FROM url WHERE Id = @Id";
				using (IDbConnection con = new MySqlConnection(ConfigurationManager.ConnectionStrings["con"].ConnectionString))
				{
					con.Open();
					var result = con.Query<url>(sql, new url { Id = id });
					return result;
				}
			}
			catch (Exception ex)
			{
				MessageBox.Show(ex.Message);
			}
			return null;
		}
	}
}
