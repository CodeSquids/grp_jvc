import mysql.connector
from mysql.connector import Error
import time
import threading


class _LockedCursor:
    def __init__(self, cursor, lock):
        self._cursor = cursor
        self._lock = lock
        self._closed = False

    def __getattr__(self, item):
        return getattr(self._cursor, item)

    def close(self):
        if self._closed:
            return

        try:
            self._cursor.close()
        finally:
            self._closed = True
            self._lock.release()

class Database:
    _instance = None
    
    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance.connection = None
            cls._instance._lock = threading.Lock()
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
            self._lock.acquire()
            try:
                # Test simple de la connexion sans utiliser is_connected()
                if self.connection is None:
                    self.connect()
                if self.connection is None:
                    raise Exception("MySQL Connection not available")

                # Retourner un curseur dédié et garder le verrou jusqu'au close()
                cursor = self.connection.cursor(dictionary=True)
                return _LockedCursor(cursor, self._lock)
            except (Error, AttributeError, IndexError, TypeError) as e:
                print(f"Erreur de connexion (tentative {attempt+1}/{max_retries}): {e}")
                self._lock.release()
                self.connect()
                time.sleep(0.5)
            except Exception as e:
                print(f"Erreur de connexion (tentative {attempt+1}/{max_retries}): {e}")
                self._lock.release()
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
