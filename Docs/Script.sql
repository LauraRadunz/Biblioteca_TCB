create database Biblioteca;
use Biblioteca;

create table Cliente (
	idCliente int auto_increment primary key,
    nome varchar(100) not null,
    telefone varchar(12) not null,
    cpf varchar(12) not null unique
);

create table Livro (
    idLivro int auto_increment primary key,
    nome varchar(100) not null,
	ano int,
    autor varchar(100),
    total int not null,
    emprestados int not null
);

create table Funcionario (
    idFuncionario int auto_increment primary key,
    nome varchar(100) not null,
    salario double not null
);

create table Emprestimo (
    idEmprestimo int auto_increment primary key,
    idCliente int not null,
    idLivro int not null,
    idFuncionario int not null,
    dataEmprestimo date not null,
    dataDevolucaoPrevista date,
    dataDevolucaoEfetiva date,
    devolvido boolean,
    renovacoes int,
    foreign key (idCliente) references Cliente(idCliente),
    foreign key (idLivro) references Livro(idLivro),
    foreign key (idFuncionario) references Funcionario(idFuncionario)
);






