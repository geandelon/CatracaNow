using CatracaNow.Arquitetura;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CatracaNow.Arquitetura
{
    public class App
    {
        private App()
        {
        }

        #region Singleton

        private static object ObjetoDeSincronia = new object();
        private static App InstanciaSolitaria = new App();

        public static App AppCorrente
        {
            get
            {
                if (InstanciaSolitaria == null)
                {
                    lock (ObjetoDeSincronia)
                    {
                        if (InstanciaSolitaria == null)
                            InstanciaSolitaria = new App();
                    }
                }
                return InstanciaSolitaria;
            }
        }

        #endregion

        private const string m_aviso = "Não foi possível localizar a configuração '{0}' no Web.config";

        public string ObtenhaStringDeConexao()
        {
            if (System.Configuration.ConfigurationManager.AppSettings.AllKeys.Contains("ConexaoCatracaNow") &&
                !String.IsNullOrEmpty(System.Configuration.ConfigurationManager.AppSettings["ConexaoCatracaNow"]))
            {
                return System.Configuration.ConfigurationManager.AppSettings["ConexaoCatracaNow"];
            }
            else
                throw new Exception(string.Format(m_aviso, "CatracaNow"));
        }

        public static void EscreveLog(string texto)
        {
            Logging.EscreveLog(texto);
        }

        public static void EscreveLogErro(Exception ex)
        {
            Logging.EscreveLog(ex);
        }

    }
}