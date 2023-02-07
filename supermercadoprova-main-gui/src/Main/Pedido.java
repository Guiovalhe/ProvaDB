package Main;

import Utils.Inputs;

import java.util.ArrayList;
import java.util.Scanner;

public class Pedido {

	private static ArrayList<Item> listaDeItens = new ArrayList();
	private static double valorTotalDoPedido = 0;

	public static void calculaValorTotal() {
		double subTotal = 0;
		for (Item item : listaDeItens) {
			subTotal += item.getValorDoItem();
		}
		valorTotalDoPedido = subTotal;
	}

// BUG CORRIGIDO - LETRA A
	/* O bug era que ao colocar no carrinho
	 * uma quantidade maior, ele dava uma mensagem
	 * negando e mesmo assim, o item entrava no 
	 * carrinho. 
	 * 
	 * Corrigindo esse primeiro bug atráves de Boolean,
	 * outro problema apareceu, o item poderia ser adicionado
	 * igual, mesmo sem estoque, porém sem dar a mensagem de 
	 * erro.
	 * 
	 * Então com o Boolean e corrigindo os IF's do código antigo,
	 * foi possivel chegar na resolução. Ao acabar o estoque do
	 * item, não é possível colocar ele no carrinho.
	 */
	public static boolean adicionaItemNaLista(Produto produto, int quantidade) {
		for (Item item : listaDeItens) {
			if (item.getProduto().getNome().equalsIgnoreCase(produto.getNome())) {
				if (Estoque.darBaixaEmEstoque(item.getProduto().getId(), quantidade)) {
					item.setQuantidade(item.getQuantidade() + quantidade);
					item.defineValorTotal();
					System.out.println("Foi adicionada a quantidade ao item já existente.");
					return true;
				}
			}
		}
		if (Estoque.darBaixaEmEstoque(produto.getId(), quantidade)) {
			listaDeItens.add(new Item(produto, quantidade));
			System.out.println("Foi adicionado o produto na lista de compras.");
			return true;
		}
		return false;
	}

	public static void imprimePedido() {
		System.out.println("                              NOTA FISCAL");
		System.out.printf("ID       |NOME            |PRECO UN           |QUANTIDADE   |PRECO ITEM \n");
		for (Item item : listaDeItens) {
			System.out.printf("%-8d | %-14s | R$%-15.2f | %-10d  | R$%.2f\n", item.getProduto().getId(),
					item.getProduto().getNome(), item.getProduto().getPreco(), item.getQuantidade(),
					item.getValorDoItem());
		}
		imprimeValorTotal();
	}

	private static void imprimeValorTotal() {
		System.out.println("\n________________________________________________________________________");
		System.out.printf("Total: R$%.2f \n\n", valorTotalDoPedido); 
		// troquei os Sysout vagos por "\n"

	}

	public static void adicionaItem() {
		String nome = recebeNomeDoTeclado();
		int quantidade = recebeQuantidadeDoTeclado();
		Produto produto = Estoque.encontraProduto(nome);
		if (produto != null) {
			adicionaItemNaLista(produto, quantidade);
			calculaValorTotal();
		} else {
			System.out.println("Produto nao encontrado");
		}

	}
// Método para o exercicio letra B e C
	/*
	 * Implementei um método de PAGAR, com arrays para determinar as 
	 * moedas e notas. Usando if e else no método, o úsuario recebe
	 * o retorno do valor do troco + quantas moedas/notas receberá
	 * Ou obtera outras 2 respostas, caso o valor informado seja menor
	 * que o valor total dos pedidos.
	 */
	public static void pagar() {
		System.out.printf("Valor total a pagar é R$%.2f \n", valorTotalDoPedido);

		System.out.println("Digite o valor em reais: ");
		double valorPago = Inputs.inputDouble();

		double troco = valorPago - valorTotalDoPedido;
		int[] notas = { 100, 50, 20, 10, 5, 2 };
		double[] moedas = { 1, 0.50, 0.25, 0.10, 0.05, 0.01 };
		if (troco >= 0) {
			if (troco == 0) {
				System.out.println("Pedido pago, obrigado!");
			} else {
				System.out.println("\nO troco é de R$ " + troco);
				System.out.println("Notas:");
				for (int i = 0; i < notas.length; i++) {
					int qtdNotas = (int) (troco / notas[i]);
					if (qtdNotas > 0) {
						System.out.println(qtdNotas + " nota(s) de R$ " + notas[i]);
						troco = troco - (qtdNotas * notas[i]);
					}
				}
				System.out.println("Moedas:");
				for (int i = 0; i < moedas.length; i++) {
					int qtdMoedas = (int) (troco / moedas[i]);
					if (qtdMoedas > 0) {
						System.out.println(qtdMoedas + " moeda(s) de R$ " + moedas[i]);
						troco = troco - (qtdMoedas * moedas[i]);
					}
				}

			}
			listaDeItens.clear();
			valorTotalDoPedido = 0;
			System.out.println();
		} else {
			System.out.printf("Por favor, verifique novamente seu dinheiro,\npois está faltando: R$%.2f \n", Math.abs(troco));
		}
	}

	public static String recebeNomeDoTeclado() {
		System.out.print("Digite o nome: ");
		return Inputs.inputString();
	}

	public static int recebeQuantidadeDoTeclado() {
		System.out.print("Digite a quantidade: ");
		return Inputs.inputInt();
	}

	public void limparCarrinho() {
		listaDeItens.clear();
	}

	public static ArrayList<Item> getListaDeItens() {
		return listaDeItens;
	}

	public void setListaDeItens(ArrayList<Item> listaDeItens) {
		Pedido.listaDeItens = listaDeItens;
	}

	public double getValorTotalDoPedido() {
		return valorTotalDoPedido;
	}

	public void setValorTotalDoPedido(double valorTotalDoPedido) {
		Pedido.valorTotalDoPedido = valorTotalDoPedido;
	}
}
