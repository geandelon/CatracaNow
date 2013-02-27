using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CatracaNow.Negocio
{
    public class ControleDeAcesso
    {
        public int Empresa { get; set; }
        public int Filial { get; set; }
        public int Codigo { get; set; }
        public string Cracha { get; set; }
        public string Nome { get; set; }
        public int UltimoAcesso { get; set; }
        public DateTime UltimoAcessoData { get; set; }
        public string UltimoAcessoDataString { get; set; }
    }
}