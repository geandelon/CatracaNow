using CatracaNow.Arquitetura;
using CatracaNow.Negocio;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace CatracaNow.Persistencia
{
    public class MapeadorControleDeAcesso
    {

        #region Construtores

        public MapeadorControleDeAcesso()
        {

        }

        private static object ObjetoDeSincronia = new object();
        private static MapeadorControleDeAcesso InstanciaSolitaria;

        public static MapeadorControleDeAcesso getInstancia()
        {
            if (InstanciaSolitaria == null)
            {
                lock (ObjetoDeSincronia)
                {
                    if (InstanciaSolitaria == null)
                        InstanciaSolitaria = new MapeadorControleDeAcesso();
                }
            }
            return InstanciaSolitaria;
        }

        #endregion

        public ControleDeAcesso Consulte(int pEmpresa, int pFilial, int pCodigoCracha)
        {
            string sql = " Select       *                           " +
                         " From         CACESS_Pessoas              " +
                         " Where        Empresa = @Empresa And      " +
                         "              Filial = @Filial And        " +
                         "              Codigo = @CodigoCracha      ";

            SqlCommand comando = new SqlCommand(sql);
            comando.Parameters.Add("@Empresa", SqlDbType.Int).Value = pEmpresa;
            comando.Parameters.Add("@Filial", SqlDbType.Int).Value = pFilial;
            comando.Parameters.Add("@CodigoCracha", SqlDbType.Int).Value = pCodigoCracha;

            return this.carregarLista(comando).SingleOrDefault();
        }

        private List<ControleDeAcesso> carregarLista(SqlCommand comando)
        {
            List<ControleDeAcesso> lista = new List<ControleDeAcesso>();

            foreach (DataRow row in Conexao.obtenhaDataTable(comando).Rows)
                lista.Add(this.montarObjeto(row));

            return lista;
        }

        private ControleDeAcesso montarObjeto(DataRow row)
        {
            ControleDeAcesso controle = new ControleDeAcesso();

            if (row.Table.Columns.Contains("Empresa") && !row.IsNull("Empresa"))
                controle.Empresa = (int)row["Empresa"];

            if (row.Table.Columns.Contains("Filial") && !row.IsNull("Filial"))
                controle.Filial = (int)row["Filial"];

            if (row.Table.Columns.Contains("Codigo") && !row.IsNull("Codigo"))
                controle.Codigo = (int)row["Codigo"];

            if (row.Table.Columns.Contains("Cracha") && !row.IsNull("Cracha"))
                controle.Cracha = (string)row["Cracha"];

            if (row.Table.Columns.Contains("Nome") && !row.IsNull("Nome"))
                controle.Nome = (string)row["Nome"];

            if (row.Table.Columns.Contains("Ultimo_Acesso") && !row.IsNull("Ultimo_Acesso"))
                controle.UltimoAcesso = (int)row["Ultimo_Acesso"];

            if (row.Table.Columns.Contains("Ultimo_Acesso_Data") && !row.IsNull("Ultimo_Acesso_Data"))
            {
                controle.UltimoAcessoData = (DateTime)row["Ultimo_Acesso_Data"];
                controle.UltimoAcessoDataString = controle.UltimoAcessoData.ToUniversalTime().ToString("dd/MM/yyyy HH:mm:ss");
            }

            return controle;
        }

    }
}