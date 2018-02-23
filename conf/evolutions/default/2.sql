# --- Library dataset

# --- !Ups

insert into author (name) values ('Amir Ghaffari');
insert into author (name) values ('Ronald McDonald');
insert into author (name) values ('Philip Holm');
insert into author (name) values ('Steve Ceolin');
insert into author (name) values ('Robert Rogers');
insert into author (name) values ('Steve Redford');
insert into author (name) values ('Stephen Schneider');
insert into author (name) values ('Maria Moyer');

insert into member (name) values ('Alan Smith');
insert into member (name) values ('Ryan Kelly');
insert into member (name) values('Joe Keery');

insert into book (name, publish_date, member_id) values ( 'Learn Play Framework!', '2017-11-01', NULL );
insert into book (name, publish_date, member_id) values ( 'Scalable Web Applications', '2017-11-02', 2 );
insert into book (name, publish_date, member_id) values ( 'Scalable Web Services', '2017-11-03', 3 );
insert into book (name, publish_date, member_id) values ( 'Play Framework Essentials', '2017-11-04', NULL);
insert into book (name, publish_date, member_id) values ( 'Scaling Reliably', '2017-11-05', 1 );
insert into book (name, publish_date, member_id) values ( 'Web Programming', '2015-10-15', NULL );
insert into book (name, publish_date, member_id) values ( 'Functional Programming', '2016-04-08', NULL );
insert into book (name, publish_date, member_id) values ( 'Parallel Programming', '2015-02-19', NULL );
insert into book (name, publish_date, member_id) values ( 'Actor Frameworks', '2017-02-06', NULL );
insert into book (name, publish_date, member_id) values ( 'Distributed Programming', '2016-04-29', NULL );

insert into author_book (author_id, book_id) values ( 1, 1);
insert into author_book (author_id, book_id) values ( 2, 2);
insert into author_book (author_id, book_id) values ( 1, 3);
insert into author_book (author_id, book_id) values ( 3, 3);
insert into author_book (author_id, book_id) values ( 2, 4);
insert into author_book (author_id, book_id) values ( 4, 4);
insert into author_book (author_id, book_id) values ( 4, 5);
insert into author_book (author_id, book_id) values ( 5, 6);
insert into author_book (author_id, book_id) values ( 2, 6);
insert into author_book (author_id, book_id) values ( 2, 7);
insert into author_book (author_id, book_id) values ( 4, 7);
insert into author_book (author_id, book_id) values ( 6, 7);
insert into author_book (author_id, book_id) values ( 3, 8);
insert into author_book (author_id, book_id) values ( 4, 8);
insert into author_book (author_id, book_id) values ( 8, 8);
insert into author_book (author_id, book_id) values ( 1, 9);
insert into author_book (author_id, book_id) values ( 2, 9);
insert into author_book (author_id, book_id) values ( 5, 9);
insert into author_book (author_id, book_id) values ( 6, 9);
insert into author_book (author_id, book_id) values ( 8, 10);
# --- !Downs

delete from author_book;
delete from author;
delete from book;
delete from member;
