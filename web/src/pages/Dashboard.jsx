import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { userAPI, authAPI } from '../services/api';
import './Dashboard.css';

const Dashboard = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      const response = await userAPI.getProfile();
      setProfile(response.data.data);
    } catch (err) {
      setError('Failed to load profile');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = async () => {
    try {
      await authAPI.logout();
    } catch (err) {
      // Even if API call fails, clear local state
    }
    logout();
    navigate('/login');
  };

  if (loading) {
    return (
      <div className="dashboard-container">
        <div className="loading">Loading profile...</div>
      </div>
    );
  }

  return (
    <div className="dashboard-container">
      <nav className="dashboard-nav">
        <div className="nav-brand">
          <h1>🏥 HealthGate</h1>
        </div>
        <div className="nav-actions">
          <span className="nav-user">
            Welcome, {user?.firstName || profile?.firstName || 'User'}!
          </span>
          <button onClick={handleLogout} className="btn btn-logout">
            Logout
          </button>
        </div>
      </nav>

      <main className="dashboard-main">
        <div className="dashboard-content">
          <h2>Dashboard</h2>

          {error && <div className="alert alert-error">{error}</div>}

          <div className="profile-card">
            <div className="profile-avatar">
              {(profile?.firstName?.[0] || user?.firstName?.[0] || 'U').toUpperCase()}
              {(profile?.lastName?.[0] || user?.lastName?.[0] || '').toUpperCase()}
            </div>
            <h3>Profile Information</h3>

            <div className="profile-details">
              <div className="profile-field">
                <label>First Name</label>
                <p>{profile?.firstName || user?.firstName || 'N/A'}</p>
              </div>
              <div className="profile-field">
                <label>Last Name</label>
                <p>{profile?.lastName || user?.lastName || 'N/A'}</p>
              </div>
              <div className="profile-field">
                <label>Email</label>
                <p>{profile?.email || user?.email || 'N/A'}</p>
              </div>
              <div className="profile-field">
                <label>Member Since</label>
                <p>
                  {profile?.createdAt
                    ? new Date(profile.createdAt).toLocaleDateString('en-US', {
                        year: 'numeric',
                        month: 'long',
                        day: 'numeric',
                      })
                    : 'N/A'}
                </p>
              </div>
              <div className="profile-field">
                <label>Last Updated</label>
                <p>
                  {profile?.updatedAt
                    ? new Date(profile.updatedAt).toLocaleDateString('en-US', {
                        year: 'numeric',
                        month: 'long',
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit',
                      })
                    : 'N/A'}
                </p>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;
