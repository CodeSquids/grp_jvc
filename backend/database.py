import mysql.connector
from mysql.connector import Error

class Database:
    _instance = None
    
    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance.connection = None
            cls._instance.connect()
        return cls._instance
    
    def connect(self):
        try:
            self.connection = mysql.connector.connect(
                host='localhost',
                database='gestion_visites',
                user='root',
                password=''  # Mettez votre mot de passe MySQL
            )
            print("Connexion à MySQL réussie")
        except Error as e:
            print(f"Erreur de connexion: {e}")
    
    def get_cursor(self):
        if not self.connection or not self.connection.is_connected():
            self.connect()
        return self.connection.cursor(dictionary=True)
    
    def commit(self):
        if self.connection:
            self.connection.commit()
    
    def close(self):
        if self.connection and self.connection.is_connected():
            self.connection.close()