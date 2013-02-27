using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Runtime.Remoting.Messaging;
using System.Web;

namespace CatracaNow.Arquitetura
{
    public class Conexao : IDisposable
    {
        SqlConnection m_Conexao;
		SqlTransaction m_Transacao;
		private const string CALLCONTEXT = "CatracaNow.Conexao";
		private static object ObjetoDeSincronia = new object();

        private Conexao()
		{
		}
		
		public static Conexao getInstancia()
		{
            Conexao instancia = (Conexao)CallContext.GetData(CALLCONTEXT);

			if (instancia == null)
			{
				lock (ObjetoDeSincronia)
				{
                    instancia = (Conexao)CallContext.GetData(CALLCONTEXT);

					if (instancia == null)
					{
                        instancia = new Conexao();
						CallContext.SetData(CALLCONTEXT, instancia);
					}
				}
			}

			return instancia;
		}

		public SqlTransaction CriarTransacao()
		{
			this.m_Transacao = this.p_ObtenhaConexao().BeginTransaction(IsolationLevel.ReadCommitted);
			return this.m_Transacao;
		}

		#region Membros de IDisposable

		void IDisposable.Dispose()
		{
            lock (ObjetoDeSincronia)
            {
                if (CallContext.GetData(CALLCONTEXT) != null)
                {
                    Conexao db = Conexao.getInstancia();
                    if (db.m_Conexao != null)
                    {
                        db.m_Conexao.Dispose();
                    }
                }
                CallContext.SetData(CALLCONTEXT, null);
            }
		}

		#endregion


		private static void OpenConnection(SqlConnection Conexao)
		{
			try
			{
				Conexao.Open();
			}
			catch (Exception ex)
			{
				throw new Exception(String.Format("Não foi possível conectar no banco de dados {0} - {1}. {2}", Conexao.DataSource, Conexao.Database, ex.Message), ex);
			}
		}

		public static SqlConnection ObtenhaConexao()
		{
			if (CallContext.GetData(CALLCONTEXT) == null)
			{
                SqlConnection conexao = new SqlConnection(App.AppCorrente.ObtenhaStringDeConexao());
				OpenConnection(conexao);
				return conexao;
			}

			return getInstancia().p_ObtenhaConexao();
		}

		private SqlConnection p_ObtenhaConexao()
		{
			if (this.m_Conexao != null)
				return this.m_Conexao;

            this.m_Conexao = new SqlConnection(App.AppCorrente.ObtenhaStringDeConexao());
			OpenConnection(this.m_Conexao);
			return m_Conexao;
		}

		private static void MontarConexao(SqlCommand pComando)
		{
            pComando.CommandTimeout = 0;

			if (CallContext.GetData(CALLCONTEXT) == null)
			{
                pComando.Connection = new SqlConnection(App.AppCorrente.ObtenhaStringDeConexao());
				OpenConnection(pComando.Connection);
			}
			else
			{
				Conexao dbUtil = Conexao.getInstancia();

                pComando.Connection = dbUtil.p_ObtenhaConexao();
                pComando.Transaction = dbUtil.m_Transacao;
			}
		}

		public static int ExecuteSQL(SqlCommand pComando)
		{
			MontarConexao(pComando);

			try
			{
				return pComando.ExecuteNonQuery();
			}
			catch (Exception ex)
			{
                App.EscreveLogErro(ex);
				Erro(ex);
			}
			finally
			{
				Finalize(pComando);
			}
			return 0;
		}

		public static object ExecuteEscalar(SqlCommand pComando)
		{
			MontarConexao(pComando);

			try
			{
				return pComando.ExecuteScalar();
			}
			catch (Exception ex)
			{
                App.EscreveLogErro(ex);
				Erro(ex);
			}
			finally
			{
				Finalize(pComando);
			}
			return null;
		}

		public static int ExecuteInsert(SqlCommand pComando)
		{
			MontarConexao(pComando);

			try
			{
				pComando.CommandText += " Select SCOPE_IDENTITY()";
				return Convert.ToInt32(pComando.ExecuteScalar());
			}
			catch (Exception ex)
			{
                App.EscreveLogErro(ex);
				Erro(ex);
			}
			finally
			{
				Finalize(pComando);
			}
			return 0;
		}

        public static int ExecuteInsert(SqlCommand pComando, DataTable pTabela, int pLote, UpdateRowSource pUpdateRowSource)
        {
            MontarConexao(pComando);

            try
            {
                SqlDataAdapter da = new SqlDataAdapter();
                da.InsertCommand = pComando;
                da.InsertCommand.UpdatedRowSource = pUpdateRowSource;
                da.UpdateBatchSize = pLote;
                return da.Update(pTabela);
            }
            catch (Exception ex)
            {
                App.EscreveLogErro(ex);
                Erro(ex);
            }
            finally
            {
                Finalize(pComando);
            }
            return 0;
        }

		public static SqlDataReader ExecuteSelectSQL(SqlCommand pComando)
		{
			try
			{
                pComando.CommandTimeout = 0;
				return pComando.ExecuteReader(CommandBehavior.CloseConnection);
			}
			catch (Exception ex)
			{
				App.EscreveLogErro(ex);
				throw ex;
			}
			finally
			{
				pComando.Dispose();
			}
		}

		public static DataTable obtenhaDataTable(SqlCommand pComando)
		{
			MontarConexao(pComando);
			DataTable dt = new DataTable();

			try
			{
				SqlDataAdapter da = new SqlDataAdapter(pComando);
				da.Fill(dt);
				return dt;
			}
			catch (Exception ex)
			{
                App.EscreveLogErro(ex);
				Erro(ex);
			}
			finally
			{
				Finalize(pComando);
			}
			return null;
		}

		private static void Erro(Exception pException)
		{
			throw pException;
		}


		private static void Finalize(SqlCommand pComando)
		{
			if (CallContext.GetData(CALLCONTEXT) == null)
				pComando.Connection.Dispose();

			pComando.Connection = null;
			pComando.Transaction = null;
		}
    }
}