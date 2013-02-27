using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;

namespace CatracaNow.Arquitetura
{
    public class Logging
    {

        private const string KEY_SETTINGS = @"SOFTWARE\SPAB\SETTINGS";
        private const string LOG_DIR = @"\log\";

        public static Logging Instancia()
        {
            return new Logging();
        }
        /// <summary>
        /// Esta classe é static
        /// </summary>
        private Logging()
        {
        }

        public static string MontarMensagemDeErro(string pMsg)
        {
            return pMsg + "\nUm log foi registrado.";
        }

        public static void EscreveLog(string pMsg)
        {
            EscreveLog(pMsg, "INFO");
        }

        public static void EscreveLog(string pMsg, Exception ex)
        {
            EscreveLog(pMsg, "ERRO");
            EscreveLog(ex);
        }

        public static void EscreveLog(Exception ex)
        {
            if (ex != null)
            {
                EscreveLog("INICIO DE TRATAMENTO ERRO", "ERRO");
                EscreveLog(ex.Message, "ERRO MESSAGE: ");
                EscreveLog(ex.StackTrace, "ERRO STACKTRACE: ");
                EscreveLog(ex.ToString(), "ERRO TOSTRING: ");
                if (ex.InnerException != null)
                    EscreveLog(ex.InnerException.ToString(), "ERRO INNEREXCEPTION: ");
                if (ex.TargetSite != null)
                    EscreveLog(ex.TargetSite.ToString(), "ERRO TARGETSITE: ");
                EscreveLog("FIM DE TRATAMENTO ERRO", "ERRO");
            }
        }

        public static void EscreveLog(string pMsg, string tipo)
        {
            try
            {
                string caminhoArquivo = LogPath;

                string stringDataHora = DateTime.Now.ToString("HH:mm:ss");

                TextWriter arquivo = File.AppendText(caminhoArquivo);

                arquivo.WriteLine(stringDataHora + " " + tipo + " " + pMsg);

                arquivo.Close();
            }
            catch (Exception)
            {

            }
        }


        public static string LogPath
        {
            get
            {
                string folderPath = System.Web.Hosting.HostingEnvironment.MapPath("~/log/");

                if (!Directory.Exists(folderPath))
                {
                    Directory.CreateDirectory(folderPath);
                }

                string logPath = folderPath + "Suite_" + DateTime.Now.ToString("yyyyMMdd") + ".log";
                return logPath;
            }
        }

        /// <summary>
        /// Limpa os logs da data informada para traz
        /// </summary>
        /// <param name="dataLog">Data do Log</param>        
        public static string LimpezaLog(DateTime dataLogExcluir)
        {
            string folderPath = System.Web.Hosting.HostingEnvironment.MapPath("~/log/");
            string[] listaArquivos = Directory.GetFiles(folderPath);
            List<string> listErroRetorno = new List<string>();
            FileInfo file;
            string nomeArquivo = "";

            foreach (string item in listaArquivos)
            {
                try
                {
                    file = new FileInfo(item);
                    nomeArquivo = file.Name;

                    DateTime dataModificacaoLog = file.LastWriteTime;
                    if (dataModificacaoLog <= dataLogExcluir)
                        file.Delete();
                }
                catch (Exception o)
                {
                    listErroRetorno.Add("Erro ao excluir o log " + nomeArquivo + " | " + o.Message);
                    Logging.EscreveLog("Erro ao excluir o log " + nomeArquivo + " | " + o.Message, "ERRO");
                }
            }

            string retorno = "";
            foreach (string item in listErroRetorno)
            {
                retorno += item;
            }

            return retorno;

        }
    }
}