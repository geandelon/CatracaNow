using CatracaNow.Negocio;
using CatracaNow.Persistencia;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Script.Services;
using System.Web.Services;

namespace CatracaNow.Servicos
{
    /// <summary>
    /// Summary description for ServicoControleDeAcesso
    /// </summary>
    [WebService(Namespace = "http://catracaNow.net/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    [ScriptService]
    public class ServicoControleDeAcesso : System.Web.Services.WebService
    {

        [WebMethod]
        [ScriptMethod(ResponseFormat = ResponseFormat.Json)]
        public List<ControleDeAcesso> HelloWorld()
        {
            List<ControleDeAcesso> pessoas = carregarPessoas();
            List<ControleDeAcesso> listaNova = new List<ControleDeAcesso>();
            MapeadorControleDeAcesso mapeadorControle = MapeadorControleDeAcesso.getInstancia();

            foreach (ControleDeAcesso controle in pessoas)
                listaNova.Add(mapeadorControle.Consulte(controle.Empresa, controle.Filial, controle.Codigo));

            return listaNova;
        }

        private List<ControleDeAcesso> carregarPessoas()
        {
            List<ControleDeAcesso> lista = new List<ControleDeAcesso>();
            ControleDeAcesso controle = new ControleDeAcesso();

            // R
            controle.Empresa = 1;
            controle.Filial = 1;
            controle.Codigo = 1852;
            lista.Add(controle);

            // M
            controle = new ControleDeAcesso();
            controle.Empresa = 1;
            controle.Filial = 1;
            controle.Codigo = 2326;
            lista.Add(controle);

            // G
            controle = new ControleDeAcesso();
            controle.Empresa = 1;
            controle.Filial = 1;
            controle.Codigo = 2564;
            lista.Add(controle);

            // I
            controle = new ControleDeAcesso();
            controle.Empresa = 1;
            controle.Filial = 1;
            controle.Codigo = 2258;
            lista.Add(controle);

            // D
            controle = new ControleDeAcesso();
            controle.Empresa = 16;
            controle.Filial = 1;
            controle.Codigo = 12;
            lista.Add(controle);

            // A
            controle = new ControleDeAcesso();
            controle.Empresa = 1;
            controle.Filial = 1;
            controle.Codigo = 2560;
            lista.Add(controle);

            return lista;
        }
    }
}
