import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {
    private int totalCandidatos = 0;

    private int candidatosQA = 0;
    private int candidatosMobile = 0;
    private int candidatosWeb = 0;

    private int maiorIdadeQa = Integer.MIN_VALUE;
    private int maiorIdadeMobile = Integer.MIN_VALUE;
    private int maiorIdadeWeb = Integer.MIN_VALUE;

    private int menorIdadeQa = Integer.MAX_VALUE;
    private int menorIdadeMobile = Integer.MAX_VALUE;
    private int menorIdadeWeb = Integer.MAX_VALUE;

    private int somaIdadeQa = 0;
    private int somaIdadeMobile = 0;
    private int somaIdadeWeb = 0;

    private Set<String> estados = new HashSet<>();

    private List<Candidato> candidatos = new ArrayList<>();

    // Exibir resultados
    public static void main(String[] args) {
        String filepath = "Academy_candidatos.txt";
        App app = new App();
        app.processarArquivo(filepath);

        app.calcularPorcentagemVaga();
        app.calcularIdadeMedia();
        app.maisVelho();
        app.maisNovo();
        app.somaIdades();
        app.exibirQuantidadeEstadosDistintos();
        app.gerarCSV();
        app.instrutorQa();
        app.instrutorMobile();
    }

    // Processar o arquivo
    private void processarArquivo(String filepath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                processarLinha(line);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    // Ler linha por linha
    private void processarLinha(String line) {
        String[] campos = line.split(";");

        String nome = campos[0].trim();
        String idadeStr = campos[1].trim();
        String vaga = campos[2].trim();
        String estado = campos[3].trim();

        if (!idadeStr.matches("\\d+ anos")) {
            return;
        }

        try {
            totalCandidatos++;
            int idade = converterIdade(idadeStr);

            contarCandidatos(vaga, idade);
            contarEstadosDistintos(estado);

            candidatos.add(new Candidato(nome, idade, vaga, estado));

        } catch (NumberFormatException e) {
            System.out.println("Erro ao converter idade: " + idadeStr);
        }
    }

    // Converter a string da idade em um inteiro
    private int converterIdade(String idadeStr) {
        return Integer.parseInt(idadeStr.replace(" anos", "").trim());
    }

    // Contadores
    private void contarCandidatos(String vaga, int idade) {
        switch (vaga) {
            case "QA":
                candidatosQA++;
                somaIdadeQa += idade;
                maiorIdadeQa = Math.max(maiorIdadeQa, idade);
                menorIdadeQa = Math.min(menorIdadeQa, idade);
                break;
            case "Mobile":
                candidatosMobile++;
                somaIdadeMobile += idade;
                maiorIdadeMobile = Math.max(maiorIdadeMobile, idade);
                menorIdadeMobile = Math.min(menorIdadeMobile, idade);
                break;
            case "Web":
                candidatosWeb++;
                somaIdadeWeb += idade;
                maiorIdadeWeb = Math.max(maiorIdadeWeb, idade);
                menorIdadeWeb = Math.min(menorIdadeWeb, idade);
                break;
            default:
                System.out.println("Vaga desconhecida: " + vaga);
                break;
        }
    }

    private void contarEstadosDistintos(String estado) {
        estados.add(estado);
    }

    // Calcular a porcentagem de pessoas por vaga
    private void calcularPorcentagemVaga() {
        System.out.println("1- Porcentagem de candidatos por vaga: ");

        double porcentagemQA = (candidatosQA / (double) totalCandidatos) * 100;
        double porcentagemMobile = (candidatosMobile / (double) totalCandidatos) * 100;
        double porcentagemWeb = (candidatosWeb / (double) totalCandidatos) * 100;

        System.out.printf("Vaga QA: %.2f%%\n", porcentagemQA);
        System.out.printf("Vaga Mobile: %.2f%%\n", porcentagemMobile);
        System.out.printf("Vaga Web: %.2f%%\n", porcentagemWeb);

    }

    // Calcular a idade média por vaga
    private void calcularIdadeMedia() {
        System.out.println("2- Idade média candidatos por vaga: ");

        double mediaIdadeQA = candidatosQA > 0 ? (somaIdadeQa / (double) candidatosQA) : 0;
        double mediaIdadeMobile = candidatosMobile > 0 ? (somaIdadeMobile / (double) candidatosMobile) : 0;
        double mediaIdadeWeb = candidatosWeb > 0 ? (somaIdadeWeb / (double) candidatosWeb) : 0;

        System.out.printf("Idade média QA: %.2f anos\n", mediaIdadeQA);
        System.out.printf("Idade média Mobile: %.2f anos\n", mediaIdadeMobile);
        System.out.printf("Idade média Web: %.2f anos\n", mediaIdadeWeb);

        System.out.println("Nenhum candidato foi registrado.");

    }

    // Candidato mais velho por vaga
    private void maisVelho() {
        System.out.println("3- Candidato mais velho por vaga");

        System.out.printf("Idade máxima QA: %d anos\n", maiorIdadeQa);

        System.out.printf("Idade máxima Mobile: %d anos\n", maiorIdadeMobile);

        System.out.printf("Idade máxima Web: %d anos\n", maiorIdadeWeb);

    }

    // Candidato mais novo por vaga
    private void maisNovo() {
        System.out.println("4- Candidato mais novo por vaga");

        System.out.printf("Idade mínima QA: %d anos\n", menorIdadeQa);

        System.out.printf("Idade mínima Mobile: %d anos\n", menorIdadeMobile);

        System.out.printf("Idade mínima Web: %d anos\n", menorIdadeWeb);

    }

    // Soma de todas as idades por vaga
    private void somaIdades() {
        System.out.println("5- Soma da idade de todos os candidatos por vaga");

        System.out.printf("Soma das idades QA: %d anos\n", somaIdadeQa);

        System.out.printf("Soma das idades Mobile: %d anos\n", somaIdadeMobile);

        System.out.printf("Soma das idades Web: %d anos\n", somaIdadeWeb);

    }

    // Contar quantidade de estados distintos entre todos os candidatos
    private void exibirQuantidadeEstadosDistintos() {
        System.out.println("6- Numero de estados distintos entre todos os candidatos");

        System.out.println(estados.size());
    }

    // Gerar arquivo CSV
    private void gerarCSV() {
        String csvFilePath = "Sorted_Academy_Candidates.csv";
        try (FileWriter writer = new FileWriter(csvFilePath)) {
            writer.append("Nome,Idade,Vaga,Estado\n");

            candidatos.sort(Comparator.comparingInt(Candidato::getIdade));

            for (Candidato candidato : candidatos) {
                writer.append(candidato.getNome())
                        .append(",")
                        .append(String.valueOf(candidato.getIdade()))
                        .append(",")
                        .append(candidato.getVaga())
                        .append(",")
                        .append(candidato.getEstado())
                        .append("\n");
            }
            System.out.println("7- Criar um arquivo CSV");
            System.out.println("Arquivo CSV gerado com sucesso: " + csvFilePath);
        } catch (IOException e) {
            System.out.println("Erro ao criar arquivo CSV " + e.getMessage());
        }
    }

    // Classe para representar Cnadidato
    static class Candidato {
        private String nome;
        private int idade;
        private String vaga;
        private String estado;

        public Candidato(String nome, int idade, String vaga, String estado) {
            this.nome = nome;
            this.idade = idade;
            this.vaga = vaga;
            this.estado = estado;
        }

        public String getNome() {
            return nome;
        }

        public int getIdade() {
            return idade;
        }

        public String getVaga() {
            return vaga;
        }

        public String getEstado() {
            return estado;
        }
    }

    // Instrutor QA
    private void instrutorQa() {
        System.out.println("8- Instrutor QA com base nas pistas");

        for (Candidato candidato : candidatos) {
            if (candidato.getVaga().equals("QA") &&
                    candidato.getEstado().equals("SC") &&
                    candidato.getIdade() == 25) {
                if (isPalindromo(candidato.getNome().split(" ")[0])) {
                    System.out.println("Instruto QA encontrado: " + candidato.getNome());
                }
            }
        }
    }

    // Verificar palindromo (nome ao contrario igual ao normal)
    private boolean isPalindromo(String nome) {
        String nomeLower = nome.toLowerCase();
        int length = nomeLower.length();
        for (int i = 0; i < length / 2; i++) {
            if (nomeLower.charAt(i) != nomeLower.charAt(length - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    // Instrutor Mobile
    private void instrutorMobile() {
        System.out.println("9- Instrutor Mobile com base nas pistas");

        List<Integer> idadesValidas = List.of(30, 32, 34, 36, 38, 40);

        for (Candidato candidato : candidatos) {
            if (candidato.getVaga().equals("Mobile") &&
                    candidato.getEstado().equals("PI") &&
                    idadesValidas.contains(candidato.getIdade()) &&
                    candidato.getNome().startsWith("C")) {

                System.out.println("Intrutor Mobile encontrado: " + candidato.getNome());
            }
        }
    }
}