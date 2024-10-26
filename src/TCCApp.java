import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TCCApp extends Application {
    private Connection connection;
    private boolean isAdmin = false;

    @Override
    public void start(Stage primaryStage) {
        connectToDatabase();
        
        VBox loginLayout = new VBox(10);
        Scene loginScene = new Scene(loginLayout, 300, 200);
        loginLayout.setStyle("-fx-background-color: #2b2b2b;");
        
        TextField userField = new TextField();
        userField.setPromptText("Usuário");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Senha");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
        loginButton.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passwordField.getText().trim();
            isAdmin = authenticateUser(username, password);
            
            if (!isAdmin && username.equals("user")) {
                showAlert("Informação", "Bem-vindo, usuário! Funcionalidades restritas a leitura.");
            }
            showMainInterface(primaryStage);
        });

        loginLayout.getChildren().addAll(userField, passwordField, loginButton);
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    private void showMainInterface(Stage primaryStage) {
        VBox layout = new VBox(10);
        Scene scene = new Scene(layout, 600, 500);
        layout.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white;");
        
        // Campos para cadastro de TCC (somente para admin)
        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome do Aluno");
        
        TextField cursoField = new TextField();
        cursoField.setPromptText("Curso");

        TextField anoField = new TextField();
        anoField.setPromptText("Ano");

        TextField orientadorField = new TextField();
        orientadorField.setPromptText("Orientador");

        TextField arquivoField = new TextField();
        arquivoField.setPromptText("Caminho do Arquivo");

        TextField temaField = new TextField();
        temaField.setPromptText("Tema do TCC");

        Button cadastrarButton = new Button("Cadastrar TCC");
        cadastrarButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white;");
        cadastrarButton.setOnAction(e -> {
            String nome = nomeField.getText();
            String curso = cursoField.getText();
            String ano = anoField.getText();
            String orientador = orientadorField.getText();
            String arquivo = arquivoField.getText();
            String tema = temaField.getText();

            if (nome.isEmpty() || curso.isEmpty() || ano.isEmpty() || orientador.isEmpty() || arquivo.isEmpty() || tema.isEmpty()) {
                showAlert("Erro", "Todos os campos devem ser preenchidos.");
                return;
            }

            if (!validateYear(ano)) {
                showAlert("Erro", "O ano deve ser entre 1900 e 2100.");
                return;
            }

            cadastrarTCC(nome, curso, ano, orientador, arquivo, tema);
            showAlert("Sucesso", "TCC cadastrado com sucesso!");
            clearFields(nomeField, cursoField, anoField, orientadorField, arquivoField, temaField);
        });
        
        Button consultarButton = new Button("Consultar TCCs");
        consultarButton.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white;");
        consultarButton.setOnAction(e -> {
            List<String> tccs = consultarTCCs("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Consulta TCCs");
            alert.setHeaderText(null);
            alert.setContentText(String.join("\n", tccs));
            alert.showAndWait();
        });

        TextField pesquisaNomeField = new TextField();
        pesquisaNomeField.setPromptText("Pesquisar por Nome");

        Button pesquisarNomeButton = new Button("Pesquisar por Nome");
        pesquisarNomeButton.setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black;");
        pesquisarNomeButton.setOnAction(e -> {
            String nome = pesquisaNomeField.getText();
            if (nome.isEmpty()) {
                showAlert("Erro", "Preencha o campo de pesquisa por Nome.");
                return;
            }
            List<String> tccs = consultarTCCs(nome);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Consulta TCCs");
            alert.setHeaderText(null);
            alert.setContentText(String.join("\n", tccs));
            alert.showAndWait();
        });

        TextField pesquisaTemaField = new TextField();
        pesquisaTemaField.setPromptText("Pesquisar por Tema");

        Button pesquisarTemaButton = new Button("Pesquisar por Tema");
        pesquisarTemaButton.setStyle("-fx-background-color: #ffeb3b; -fx-text-fill: black;");
        pesquisarTemaButton.setOnAction(e -> {
            String tema = pesquisaTemaField.getText();
            if (tema.isEmpty()) {
                showAlert("Erro", "Preencha o campo de pesquisa por Tema.");
                return;
            }
            List<String> tccs = consultarTCCs(tema);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Consulta TCCs");
            alert.setHeaderText(null);
            alert.setContentText(String.join("\n", tccs));
            alert.showAndWait();
        });
        
        Button editarButton = new Button("Editar TCC");
        editarButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white;");
        editarButton.setOnAction(e -> {
            // lógica para editar TCC (implementada apenas para admin)
        });

        Button excluirButton = new Button("Excluir TCC");
        excluirButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        excluirButton.setOnAction(e -> {
            String nome = pesquisaNomeField.getText();
            if (nome.isEmpty()) {
                showAlert("Erro", "Preencha o campo de pesquisa por Nome para excluir.");
                return;
            }
            excluirTCC(nome);
            showAlert("Sucesso", "TCC excluído com sucesso!");
        });

        if (isAdmin) {
            layout.getChildren().addAll(nomeField, cursoField, anoField, orientadorField, arquivoField, temaField, cadastrarButton, editarButton, excluirButton);
        }
        layout.getChildren().addAll(consultarButton, pesquisaNomeField, pesquisarNomeButton, pesquisaTemaField, pesquisarTemaButton);

        primaryStage.setScene(scene);
        primaryStage.setTitle("TCC App");
        primaryStage.show();
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "root", "251216");
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    private boolean authenticateUser(String username, String password) {
        return username.equals("admin") && password.equals("senha_admin");
    }

    private void cadastrarTCC(String nome, String curso, String ano, String orientador, String arquivo, String tema) {
        String query = "INSERT INTO tccs (aluno_nome, curso, ano, orientador, arquivo_path, tema) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome);
            statement.setString(2, curso);
            statement.setString(3, ano);
            statement.setString(4, orientador);
            statement.setString(5, arquivo);
            statement.setString(6, tema);
            statement.executeUpdate();
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao cadastrar TCC: " + e.getMessage());
        }
    }

    private List<String> consultarTCCs(String filtro) {
        List<String> tccs = new ArrayList<>();
        String query = "SELECT aluno_nome, curso, ano, orientador, arquivo_path, tema FROM tccs WHERE aluno_nome LIKE ? OR tema LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + filtro + "%");
            statement.setString(2, "%" + filtro + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tccs.add("Nome: " + resultSet.getString("aluno_nome") + ", Curso: " + resultSet.getString("curso") + 
                          ", Ano: " + resultSet.getString("ano") + ", Orientador: " + resultSet.getString("orientador") + 
                          ", Arquivo: " + resultSet.getString("arquivo_path") + ", Tema: " + resultSet.getString("tema"));
            }
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao consultar TCCs: " + e.getMessage());
        }
        return tccs;
    }

    private void excluirTCC(String nome) {
        String query = "DELETE FROM tccs WHERE aluno_nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome);
            statement.executeUpdate();
        } catch (SQLException e) {
            showAlert("Erro", "Erro ao excluir TCC: " + e.getMessage());
        }
    }

    private boolean validateYear(String year) {
        try {
            int ano = Integer.parseInt(year);
            return ano >= 1900 && ano <= 2100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
