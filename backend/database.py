import mysql.connector
from mysql.connector import Error
import time

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
            if self.connection:
                try:
                    self.connection.close()
                except:
                    pass
            
            self.connection = mysql.connector.connect(
                host='localhost',
                database='gestion_visites',
                user='root',
                password='',  # Mettez votre mot de passe MySQL
                autocommit=True,
                pool_reset_session=True
            )
            print("Connexion à MySQL réussie")
        except Error as e:
            print(f"Erreur de connexion: {e}")
            self.connection = None
    
    def get_cursor(self):
        """Retourne un curseur en vérifiant la connexion de manière robuste"""
        max_retries = 3
        for attempt in range(max_retries):
            try:
                # Test simple de la connexion sans utiliser is_connected()
                if self.connection is None:
                    self.connect()
                
                # Tester la connexion avec une requête simple
                cursor = self.connection.cursor(dictionary=True)
                cursor.execute("SELECT 1")
                cursor.fetchone()
                
                # Retourner un nouveau curseur
                return self.connection.cursor(dictionary=True)
                
            except (Error, AttributeError, IndexError, TypeError) as e:
                print(f"Erreur de connexion (tentative {attempt+1}/{max_retries}): {e}")
                self.connect()
                time.sleep(0.5)
        
        # Si on arrive ici, la connexion a échoué
        raise Exception("Impossible d'établir une connexion à la base de données")
    
    def commit(self):
        if self.connection:
            try:
                self.connection.commit()
            except:
                pass
    
    def close(self):
        if self.connection:
            try:
                self.connection.close()
                print("Connexion MySQL fermée")
            except:
                pass